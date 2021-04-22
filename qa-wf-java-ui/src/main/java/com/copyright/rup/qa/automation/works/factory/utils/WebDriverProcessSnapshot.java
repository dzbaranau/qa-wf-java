package com.copyright.rup.qa.automation.pubportal.special.request.utils;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.pubportal.special.request.utils.dto.Processes;

import org.apache.commons.collections4.CollectionUtils;
import org.jutils.jprocesses.JProcesses;
import org.jutils.jprocesses.model.ProcessInfo;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for working with browser and web driver  processes.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Sep 15, 2020
 *
 * @author Dzmitry Padskokau
 */
public class WebDriverProcessSnapshot {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    private Processes state;
    private String browserProcessName;
    private String webDriverProcessName;

    /**
     * Constructor.
     *
     * @param browserProcessName   name of browser process name
     * @param webDriverProcessName name of web driver process name
     */
    public WebDriverProcessSnapshot(String browserProcessName, String webDriverProcessName) {
        this.browserProcessName = browserProcessName;
        this.webDriverProcessName = webDriverProcessName;
    }

    /**
     * Constructor.
     *
     * @param processes state of processes
     */
    public WebDriverProcessSnapshot(Processes processes) {
        this.state = processes;
        this.browserProcessName = processes.getBrowserProcessName();
        this.webDriverProcessName = processes.getWebDriverProcessName();
    }

    /**
     * Updates a list of PIDs for all running browser and web driver processes.
     */
    public void updateRunningProcesses() {
        state = getCurrentProcesses();
        LOGGER.info("Found processes: {}", state);
    }

    /**
     * Saves difference between the current list of processes and previous state.
     */
    public void saveDiffWithCurrentProcesses() {
        Processes diff = diffWithCurrentProcesses(state);
        LOGGER.info("Saving new processes to file: {}", diff);
        saveProcessesToFile(diff);
    }

    /**
     * Kills processes that are still running from the previous bdd task.
     */
    public void killRemainProcesses() {
        Processes currentProcesses = getCurrentProcesses();
        List<String> browserPIDs = intersectArrays(currentProcesses.getBrowserPIDs(), state.getBrowserPIDs());
        List<String> webDriverPIDs = intersectArrays(currentProcesses.getWebDriverPIDs(), state.getWebDriverPIDs());
        kill(browserPIDs);
        kill(webDriverPIDs);
    }

    private static void kill(List<String> pids) {
        pids.forEach(process -> {
            LOGGER.info("killing [{}] process", process);
            JProcesses.killProcess(Integer.parseInt(process));
        });
    }

    private static <T> ArrayList<T> subtractArrays(List<T> array1, List<T> array2) {
        return new ArrayList<>(CollectionUtils.subtract(array1, array2));
    }

    private static <T> ArrayList<T> intersectArrays(List<T> array1, List<T> array2) {
        return new ArrayList<>(CollectionUtils.intersection(array1, array2));
    }

    private static void saveProcessesToFile(Processes processes) {
        LOGGER.info("Saving processes to file");
        WebDriverProcessesUtils.toFile(processes);
    }

    private static List<String> processesList(String processName) {
        List<ProcessInfo> processesList = getJProcesses().listProcesses(processName);
        return processesList.stream()
                .map(ProcessInfo::getPid)
                .collect(Collectors.toList());
    }

    private static JProcesses getJProcesses() {
        return JProcesses.get().fastMode(true);
    }

    private Processes diffWithCurrentProcesses(Processes processes) {
        Processes currentProcesses = getCurrentProcesses();
        Processes result = new Processes();
        result.setBrowserPIDs(subtractArrays(currentProcesses.getBrowserPIDs(), processes.getBrowserPIDs()));
        result.setWebDriverPIDs(subtractArrays(currentProcesses.getWebDriverPIDs(), processes.getWebDriverPIDs()));
        result.setBrowserProcessName(browserProcessName);
        result.setWebDriverProcessName(webDriverProcessName);
        return result;
    }

    private Processes getCurrentProcesses() {
        List<String> chromePIDs = processesList(browserProcessName);
        List<String> webDriverPIDs = processesList(webDriverProcessName);
        return new Processes(chromePIDs, webDriverPIDs, browserProcessName, webDriverProcessName);
    }
}
