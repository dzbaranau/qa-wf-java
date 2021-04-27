package com.copyright.rup.qa.automation.works.factory.utils;

import java.nio.file.Paths;

/**
 * Common test parameters support class.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: March 4, 2017
 *
 * @author Kiryl Milavidau
 */
public final class TestParameters {

    /**
     * The constant SYSTEM_ENVIRONMENT.
     */
    public static final String SYSTEM_ENVIRONMENT = System.getProperty("qaa.environment", "local");

    /**
     * The constant CROSS_BROWSER.
     */
    public static final String CROSS_BROWSER = System.getProperty("qaa.crossBrowser", "false");

    /**
     * The constant STORY_TO_RUN.
     */
    public static final String STORY_TO_RUN = System.getProperty("qaa.story", "*");

    /**
     * The constant META_FILTERS.
     */
    public static final String META_FILTERS =
            System.getProperty("qaa.metaFilters", "-make-screenshot -ignore -ignoreUsualRun");

    /**
     * The constant BROWSER.
     */
    public static final BrowserType BROWSER = BrowserType.resolve(System.getProperty("qaa.browser", "chrome"));

    /**
     * The constant HEADLESS_MODE.
     */
    public static final String HEADLESS_MODE = System.getProperty("qaa.headless.mode", "false");

    /**
     * The constant MAKE_SCREENSHOT_META.
     */
    public static final String MAKE_SCREENSHOT_META = "+make-screenshot";

    /** Download path. */
    public static final String DOWNLOAD_PATH =
            Paths.get("./", "build/downloaded").normalize().toAbsolutePath().toString();

    /**
     * Version of Chrome driver.
     */
    public static final String CHROME_VERSION = System.getProperty("qaa.chrome.version");

    /**
     * The constant POOLING_INTERVAL.
     */
    public static final int POOLING_INTERVAL = 1000;

    /**
     * The constant SHORT_WAIT.
     */
    public static final int SHORT_WAIT = 4000;

    /**
     * The constant LONG_WAIT.
     */
    public static final int LONG_WAIT = 10000;

    /**
     * The constant EXTREMELY_LONG_WAIT.
     */
    public static final int EXTREMELY_LONG_WAIT = 15000;

    /**
     * The constant APP_VERSION.
     */
    public static final String APP_VERSION = System.getProperty("qaa.version");

    /**
     * Is server timezone enabled or disabled for automated tests.
     */
    public static final boolean SERVER_TIME_ENABLE =
            Boolean.parseBoolean(System.getProperty("qaa.server.time.enable", "false"));

    /**
     * Path to the directory which should contain screenshots.
     */
    public static final String SCREENSHOT_PATH = System.getProperty("qaa.screenshot.path", "build/reports/tests");

    /**
     * Port which will be used to proxy requests for browser.
     */
    public static final Integer BROWSER_PROXY_PORT =
            Integer.parseInt(System.getProperty("qaa.browser.proxy.port", "7080"));

    private TestParameters() {
    }

}
