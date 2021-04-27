package com.copyright.rup.qa.automation.works.factory.webdriver;

import static com.google.common.collect.ImmutableMap.of;

import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Default chrome web driver provider.
 * <p/>
 * Copyright (C) 2019 copyright.com
 * <p/>
 * Date: Feb 01, 2019
 *
 * @author Aliaksandr Yushkevich
 */
public class ChromeDriverProvider extends AbstractChromeDriverProvider {

    @Override
    protected ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", of("profile.default_content_settings.popups", 0,
                "download.default_directory", TestParameters.DOWNLOAD_PATH));
        options.addArguments("--test-type");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-debugging-port=9222");
        //        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-print-preview");
        options.setAcceptInsecureCerts(true);
        options.setExperimentalOption("w3c", true);

        return options;
    }
}
