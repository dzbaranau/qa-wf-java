package com.copyright.rup.qa.automation.works.factory.utils;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.utils.dto.Processes;
import org.slf4j.Logger;

/**
 * Clean browser and web driver processes that were started in the bdd task.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Sep 18, 2020
 *
 * @author Dzmitry Padskokau
 */
public class CleanWebDriverProcessesTask {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    private CleanWebDriverProcessesTask() {
        throw new UnsupportedOperationException();
    }

    /**
     * Reads running processes from a file and kills those that was not stopped yet.
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Cleaning up browser and web driver processes.");
        Processes savedProcesses = null;
        try {
            savedProcesses = WebDriverProcessesUtils.getProcessesFromFile();
        } catch (Exception e) {
            LOGGER.warn("File read error.");
        }
        if (savedProcesses != null) {
            WebDriverProcessSnapshot startedProcesses = new WebDriverProcessSnapshot(savedProcesses);
            startedProcesses.killRemainProcesses();
        }
    }
}
