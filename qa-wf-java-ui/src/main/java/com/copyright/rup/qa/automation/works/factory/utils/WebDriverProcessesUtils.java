package com.copyright.rup.qa.automation.works.factory.utils;

import com.copyright.rup.common.exception.RupRuntimeException;
import com.copyright.rup.qa.automation.pubportal.special.request.utils.dto.Processes;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Util class to work with web driver processes.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Sep 18, 2020
 *
 * @author Dzmitry Padskokau
 */
public class WebDriverProcessesUtils {

    private static final String FILENAME = "build/processesPIDs.json";

    private WebDriverProcessesUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets list of processes from file.
     *
     * @return processes list from file.
     */
    public static Processes getProcessesFromFile() {
        try {
            return new ObjectMapper().readValue(Paths.get(FILENAME).toFile(), Processes.class);
        } catch (IOException e) {
            throw new RupRuntimeException(e);
        }
    }

    /**
     * Save object to file as json.
     *
     * @param <T> object type
     * @param object object to save
     */
    public static <T> void toFile(T object) {
        try {
            new ObjectMapper().writeValue(Paths.get(FILENAME).toFile(), object);
        } catch (IOException e) {
            throw new RupRuntimeException(e);
        }
    }
}
