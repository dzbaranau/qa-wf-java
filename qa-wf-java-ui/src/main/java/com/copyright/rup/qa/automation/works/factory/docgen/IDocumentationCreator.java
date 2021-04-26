package com.copyright.rup.qa.automation.works.factory.docgen;

import com.copyright.rup.qa.automation.works.factory.docgen.dto.Node;

import java.io.IOException;

/**
 * Generate documentation file.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 14, 2021
 *
 * @author Dzmitry Padskokau
 */
public interface IDocumentationCreator {

    /**
     * Creates and saves readme.md.
     *
     * @param document steps
     * @param fileName filename to save
     * @throws IOException i/o exception
     */
    void createDocumentation(Node document, String fileName) throws IOException;
}
