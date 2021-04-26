package com.copyright.rup.qa.automation.works.factory.docgen;

import com.copyright.rup.qa.automation.pubportal.special.request.docgen.dto.ParsedClass;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Interface for parsing source steps.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 12, 2021
 *
 * @author Dzmitry Padskokau
 */
public interface IClassParser {

    /**
     * Parses class file.
     *
     * @param fileClass class
     * @return steps
     * @throws FileNotFoundException file not found
     */
    ParsedClass parse(File fileClass) throws FileNotFoundException;
}
