package com.copyright.rup.qa.automation.works.factory.utils.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Represents a list of browser and web driver PIDs.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Sep 17, 2020
 *
 * @author Dzmitry Padskokau
 */
public class Processes {

    private List<String> browserPIDs;
    private List<String> webDriverPIDs;
    private String browserProcessName;
    private String webDriverProcessName;

    /**
     * Constructor.
     */
    public Processes() {
    }

    /**
     * Constructor.
     * @param browserPIDs PID of a browser process
     * @param webDriverPIDs PID of a web driver process
     * @param browserProcessName browser process name
     * @param webDriverProcessName browser web drive name
     */
    public Processes(List<String> browserPIDs, List<String> webDriverPIDs, String browserProcessName,
            String webDriverProcessName) {
        this.browserPIDs = browserPIDs;
        this.webDriverPIDs = webDriverPIDs;
        this.browserProcessName = browserProcessName;
        this.webDriverProcessName = webDriverProcessName;
    }

    public List<String> getBrowserPIDs() {
        return browserPIDs;
    }

    public void setBrowserPIDs(List<String> browserPIDs) {
        this.browserPIDs = browserPIDs;
    }

    public List<String> getWebDriverPIDs() {
        return webDriverPIDs;
    }

    public void setWebDriverPIDs(List<String> webDriverPIDs) {
        this.webDriverPIDs = webDriverPIDs;
    }

    public String getBrowserProcessName() {
        return browserProcessName;
    }

    public void setBrowserProcessName(String browserProcessName) {
        this.browserProcessName = browserProcessName;
    }

    public String getWebDriverProcessName() {
        return webDriverProcessName;
    }

    public void setWebDriverProcessName(String webDriverProcessName) {
        this.webDriverProcessName = webDriverProcessName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Processes)) {
            return false;
        }

        Processes processes = (Processes) o;

        return new EqualsBuilder()
                .append(browserPIDs, processes.browserPIDs)
                .append(webDriverPIDs, processes.webDriverPIDs)
                .append(browserProcessName, processes.browserProcessName)
                .append(webDriverProcessName, processes.webDriverProcessName)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(browserPIDs)
                .append(webDriverPIDs)
                .append(browserProcessName)
                .append(webDriverProcessName)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("browserPIDs", browserPIDs)
                .append("webDriverPIDs", webDriverPIDs)
                .append("browserProcessName", browserProcessName)
                .append("webDriverProcessName", webDriverProcessName)
                .toString();
    }
}
