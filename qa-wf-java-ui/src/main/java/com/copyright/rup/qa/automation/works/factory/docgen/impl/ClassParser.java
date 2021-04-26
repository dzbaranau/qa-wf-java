package com.copyright.rup.qa.automation.works.factory.docgen.impl;

import com.copyright.rup.common.exception.RupRuntimeException;
import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.IClassParser;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Parameter;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.ParsedClass;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Step;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.StepType;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.StepType.ALIAS;

/**
 * Class for parsing source steps.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 12, 2021
 *
 * @author Dzmitry Padskokau
 */
public class ClassParser implements IClassParser {

    private static final Logger LOGGER = RupLogUtils.getLogger();
    private static final List<String> JBEHAVE_ANNOTATION =
            ImmutableList.of("Given", "When", "Then", "Alias", "Aliases");
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public ParsedClass parse(File fileClass) throws FileNotFoundException {
        LOGGER.debug("Parsing {}", fileClass.getName());
        JavaParser parser = new JavaParser();
        ParseResult<CompilationUnit> stepClass = parser.parse(fileClass);
        List<String> menus = getMenuHierarchy(stepClass);
        List<MethodDeclaration> methods = getAllMethodsOfClass(stepClass);
        List<Step> steps = new ArrayList<>();

        for (MethodDeclaration method : methods) {
            try {
                if (method.isPublic()) {
                    parseMethod(steps, method);
                }
            } catch (Exception e) {
                LOGGER.warn("Cannot parse [{}] method", method.getNameAsString(), e);
            }
        }
        return new ParsedClass(menus, steps);
    }

    private List<MethodDeclaration> getAllMethodsOfClass(ParseResult<CompilationUnit> stepClass) {
        return stepClass.getResult()
                .map(step -> step.findAll(MethodDeclaration.class))
                .orElse(Collections.emptyList());
    }

    private List<String> getMenuHierarchy(ParseResult<CompilationUnit> stepClass) {
        List<SingleMemberAnnotationExpr> stepsDescription = stepClass.getResult()
                .flatMap(parsedClass -> parsedClass.findFirst(ClassOrInterfaceDeclaration.class))
                .flatMap(declaration -> declaration.getAnnotationByName("StepsDescription"))
                .map(annotation -> annotation.getChildNodes().get(1))
                .map(annotation -> annotation.findAll(SingleMemberAnnotationExpr.class))
                .orElse(new ArrayList<>());
        return stepsDescription.stream()
                .map(annotation -> annotation.findFirst(StringLiteralExpr.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(StringLiteralExpr::asString)
                .collect(Collectors.toList());
    }

    private void parseMethod(List<Step> steps, MethodDeclaration method) {
        // parse jbehave annotations
        MultiValuedMap<StepType, String> annotations = extractAnnotations(method);
        if (annotations.isEmpty()) {
            return;
        }
        // parse javaDoc
        Javadoc javaDocComment = extractJavaDoc(method);
        String stepDescription = javaDocComment.getDescription().toText().replace(LINE_SEPARATOR, " ");
        List<Parameter> parameters = extractParameters(javaDocComment);

        // split steps by type
        for (Map.Entry<StepType, String> annotationWithType : annotations.entries()) {
            String definition = annotationWithType.getValue();
            if (ALIAS == annotationWithType.getKey()) {
                addAliasSteps(steps, annotations, definition, stepDescription, parameters);
            } else {
                steps.add(new Step(annotationWithType.getKey(), definition, stepDescription,
                        parameters));
            }
        }
    }

    private Javadoc extractJavaDoc(MethodDeclaration method) {
        return method.getJavadocComment()
                .map(JavadocComment::parse)
                .orElseThrow(() -> new RupRuntimeException(String.format(
                        "JavaDoc comment is not found for [%s] method declaration.", method.getDeclarationAsString())));
    }

    private List<Parameter> extractParameters(Javadoc javaDocComment) {
        return javaDocComment.getBlockTags().stream()
                .filter(v -> JavadocBlockTag.Type.PARAM == v.getType())
                .map(paramDescription -> new Parameter(paramDescription.getName().orElse(""),
                        paramDescription.getContent().toText()))
                .collect(Collectors.toList());
    }

    private void addAliasSteps(List<Step> steps, MultiValuedMap<StepType, String> annotations,
            String alias, String stepDescription, List<Parameter> parameters) {
        Set<StepType> stepTypes = annotations.keySet();
        for (StepType type : stepTypes) {
            if (type != ALIAS) {
                steps.add(new Step(type, alias, stepDescription, parameters));
            }
        }
    }

    private MultiValuedMap<StepType, String> extractAnnotations(MethodDeclaration method) {

        MultiValuedMap<StepType, String> annotations = new HashSetValuedHashMap<>();

        List<AnnotationExpr> annotationExprs = method.getAnnotations().stream()
                .filter(annotation -> JBEHAVE_ANNOTATION.contains(annotation.getNameAsString()))
                .collect(Collectors.toList());

        for (AnnotationExpr annotationExpr : annotationExprs) {
            Set<String> stepDefinitions = extractStepDefinitions(annotationExpr);
            StepType stepType = getStepType(annotationExpr);
            annotations.putAll(stepType, stepDefinitions);
        }
        return annotations;
    }

    private Set<String> extractStepDefinitions(AnnotationExpr annotationExpr) {
        Node annotationValueNode = annotationExpr.getChildNodes().get(1);
        if (annotationValueNode instanceof BinaryExpr) {
            String definition = annotationValueNode.findAll(StringLiteralExpr.class).stream()
                    .map(StringLiteralExpr::asString).reduce((v1, v2) -> v1 + v2)
                    .orElse("");
            return Collections.singleton(definition);
        }
        return annotationExpr.findAll(StringLiteralExpr.class)
                .stream()
                .map(StringLiteralExpr::asString)
                .collect(Collectors.toSet());
    }

    private StepType getStepType(AnnotationExpr annotationExpr) {
        StepType sourceType = StepType.valueOfByName(annotationExpr.getNameAsString())
                .orElseThrow(() -> new RupRuntimeException(
                        String.format("Annotation type \"%s\" not found", annotationExpr.getNameAsString())));
        return StepType.ALIASES == sourceType ? ALIAS : sourceType;
    }
}
