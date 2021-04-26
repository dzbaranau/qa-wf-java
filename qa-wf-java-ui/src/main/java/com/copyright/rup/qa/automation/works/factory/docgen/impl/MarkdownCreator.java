package com.copyright.rup.qa.automation.works.factory.docgen.impl;

import com.copyright.rup.qa.automation.pubportal.special.request.docgen.IDocumentationCreator;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Node;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Parameter;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Step;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.StepType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generate readme.md doc file.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 14, 2021
 *
 * @author Dzmitry Padskokau
 */
public class MarkdownCreator implements IDocumentationCreator {

    private static final String STEP_DESCRIPTION_TEMPLATE = "- ```%s``` - %s";
    private static final String PARAM_DESCRIPTION_TEMPLATE = "\t* ```%s``` - %s";
    private static final String PARAGRAPH_TEMPLATE = "* [%s](#%s)";
    private static final String ANCHOR_TEMPLATE = "<a name=\"%s\"/>";
    private static final String BASE_DOCUMENT = "src/main/resources/docgen/base-document.md";
    private static final String MENU_FOR_BASE_DOCUMENT = " * [Available users](#available-users)\n" +
            " * [JSON parametrization](#json-parametrization)\n" +
            " * [Organization parametrization](#org-parametrization)\n" +
            " * [Examples tables issues](#examples-tables-issues)\n" +
            " * [Creation of publication names](#creation-of-publication-names)\n\n";

    @Override
    public void createDocumentation(Node document, String fileName) throws IOException {
        StringBuilder doc = new StringBuilder("# Table of Contents\n\n");
        createTableOfContent(doc, document);
        printContent(doc, 2, document);
        fillFooter(doc);

        try (OutputStreamWriter writer =
                     new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            writer.write(doc.toString());
        }
    }

    private void createTableOfContent(StringBuilder doc, Node document) {
        fillTableOfContent(doc, 0, document);
        doc.append(MENU_FOR_BASE_DOCUMENT);
    }

    private void fillTableOfContent(StringBuilder doc, int deep, Node currentNode) {
        String indent = String.join("", Collections.nCopies(deep, "\t"));
        String anchorString = getAnchorString(currentNode);
        doc.append(indent).append(" ")
                .append(String.format(PARAGRAPH_TEMPLATE, currentNode.getSubMenu(), anchorString))
                .append("\n");
        if (CollectionUtils.isNotEmpty(currentNode.getChildren())) {
            deep++;
            for (Node child : getSortedChildren(currentNode)) {
                fillTableOfContent(doc, deep, child);
            }
        } else {
            deep--;
        }
    }

    private String getAnchorString(Node currentNode) {
        Node node = currentNode;
        StringBuilder anchor = new StringBuilder();
        if (currentNode.getParent() == null) {
            anchor.append(currentNode.getSubMenu());
        }
        while (node.getParent() != null) {
            anchor.insert(0, node.getSubMenu() + " ");
            node = node.getParent();
        }
        return anchor.toString().toLowerCase().trim().replace(" ", "-");
    }

    private void printContent(StringBuilder doc, int deep, Node currentNode) {
        String paragraph = String.join("", Collections.nCopies(deep, "#"));
        doc.append("\n");
        doc.append(String.format(ANCHOR_TEMPLATE, getAnchorString(currentNode))).append("\n\n");
        doc.append(paragraph).append(" ").append(currentNode.getSubMenu()).append("\n");
        if (CollectionUtils.isNotEmpty(currentNode.getSteps())) {
            printSteps(doc, deep, currentNode);
        }
        if (CollectionUtils.isNotEmpty(currentNode.getChildren())) {
            deep++;
            for (Node child : getSortedChildren(currentNode)) {
                printContent(doc, deep, child);
            }
        } else {
            deep--;
        }
    }

    private List<Node> getSortedChildren(Node currentNode) {
        return currentNode.getChildren().stream()
                .sorted(Comparator.comparing(Node::getSubMenu))
                .collect(Collectors.toList());
    }

    private void fillFooter(StringBuilder doc) throws IOException {
        doc.append("\n");
        doc.append(getBaseDocument());
    }

    private String getBaseDocument() throws IOException {
        File file = new File(BASE_DOCUMENT);
        return FileUtils.readFileToString(file, "UTF-8");
    }

    private void printSteps(StringBuilder doc, int deep, Node currentNode) {
        StepType type = null;
        currentNode.getSteps().sort(Comparator.comparing(Step::getType).thenComparing(Step::getDefinition));
        for (Step step : currentNode.getSteps()) {
            if (type != step.getType()) {
                doc.append("\n");
                String paragraph = String.join("", Collections.nCopies(deep + 2, "#"));
                doc.append(paragraph).append(" ").append(step.getType().getName()).append("\n");
                type = step.getType();
            }
            doc.append(String.format(STEP_DESCRIPTION_TEMPLATE, step.getDefinition(), step.getDescription()))
                    .append("\n");
            if (CollectionUtils.isNotEmpty(step.getParameters())) {
                printParams(doc, step);
            }
        }
    }

    private void printParams(StringBuilder doc, Step step) {
        for (Parameter parameter : step.getParameters()) {
            doc.append(String.format(PARAM_DESCRIPTION_TEMPLATE, parameter.getName(),
                    parameter.getDescription())).append("\n");
        }
    }
}
