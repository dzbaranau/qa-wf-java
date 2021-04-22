package com.copyright.rup.qa.automation.works.factory.webdriver;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;
import com.copyright.rup.qa.automation.pubportal.special.request.utils.WebDriverProcessSnapshot;

import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Base class to create Chrome driver provider.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Nov 06, 2020
 *
 * @author Aliaksandr Yushkevich
 */
public abstract class AbstractChromeDriverProvider implements WebDriverProvider {

    private static final String CHROME_PROCESS_NAME = "chrome";
    private static final String WEBDRIVER_PROCESS_NAME = "chromedriver";

    private static final Logger LOGGER = RupLogUtils.getLogger();

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        WebDriverProcessSnapshot processes = new WebDriverProcessSnapshot(CHROME_PROCESS_NAME, WEBDRIVER_PROCESS_NAME);
        processes.updateRunningProcesses();
        WebDriverManager.chromedriver().targetPath("./build/cache").version(TestParameters.CHROME_VERSION).setup();
        WebDriver webDriver = null;
        int retryCount = 0;
        while (null == webDriver) {
            try {
                webDriver = new ChromeDriver(getChromeOptions());
            } catch (Exception e) {
                if (retryCount > 2) {
                    throw e;
                }
                LOGGER.warn("Unable to create Chrome web driver.", e);
                retryCount++;
            }
        }
        webDriver.manage().window().maximize();
        processes.saveDiffWithCurrentProcesses();
        return webDriver;
    }

    /**
     * Provides chrome options.
     *
     * @return chrome options
     */
    protected abstract ChromeOptions getChromeOptions();
}
