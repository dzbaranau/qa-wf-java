package com.copyright.rup.qa.automation.works.factory.docgen;

import com.copyright.rup.common.exception.RupRuntimeException;
import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.Node;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.ParsedClass;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.impl.ClassParser;
import com.copyright.rup.qa.automation.pubportal.special.request.docgen.impl.MarkdownCreator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generate documentation for steps.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 6, 2021
 *
 * @author Dzmitry Padskokau
 */
public class DocumentationGenerator {

    private static final Logger LOGGER = RupLogUtils.getLogger();
    private static final String SUPPORTED_STEPS = "Supported steps";

    private DocumentationGenerator() {
    }

    /**
     * Starts doc generation.
     *
     * @param args arguments
     * @throws IOException exception
     */
    public static void main(String[] args) throws IOException {
        LOGGER.info("Starting documentation generation");
        argsValidation(args);
        String stepsPath = args[0];
        String documentPath = args[1];
        ClassParser parser = new ClassParser();
        List<ParsedClass> steps = new ArrayList<>();
        Collection<File> files = getStepsFiles(stepsPath);
        for (File file : files) {
            ParsedClass value = parser.parse(file);
            if (CollectionUtils.isNotEmpty(value.getMenuHierarchy())) {
                steps.add(value);
            }
        }
        Node node = convertToNode(steps);
        MarkdownCreator documentCreator = new MarkdownCreator();
        documentCreator.createDocumentation(node, documentPath);
    }

    private static void argsValidation(String[] args) {
        if (args.length != 2) {
            String error = "Two arguments should be passed: the path to the classes with steps and " +
                    "the path to the generated documentation file.";
            LOGGER.error(error);
            throw new RupRuntimeException(error);
        }
    }

    /**
     * Gets tree structure for menu hierarchy.
     *
     * @param steps list of steps
     * @return tree
     */
    private static Node convertToNode(List<ParsedClass> steps) {
        Node document = new Node(SUPPORTED_STEPS);
        for (ParsedClass parsedClass : steps) {
            List<String> menuHierarchy = parsedClass.getMenuHierarchy();
            int deep = 0;
            Node currentNode = document;
            while (deep < menuHierarchy.size()) {
                Node child = currentNode.getOrCreateChildByMenu(menuHierarchy.get(deep));
                if (deep == (menuHierarchy.size() - 1)) {
                    child.getSteps().addAll(parsedClass.getSteps());
                }
                currentNode = child;
                deep++;
            }
        }
        return document;
    }

    private static Collection<File> getStepsFiles(String stepsPath) {
        return FileUtils.listFiles(new File(stepsPath), new WildcardFileFilter("*Steps.java"), TrueFileFilter.TRUE);
    }
}
