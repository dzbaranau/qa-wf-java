package com.copyright.rup.qa.automation.works.factory.webdriver;

import static com.google.common.collect.ImmutableMap.of;

import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Chrome we driver provider with traffic proxying.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Nov 06, 2020
 *
 * @author Aliaksandr Yushkevich
 */
public class ProxiedChromeDriverProvider extends AbstractChromeDriverProvider {

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
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-web-security");
        options.addArguments(
                String.format("--proxy-server=http://127.0.0.1:%s", TestParameters.BROWSER_PROXY_PORT));
        options.setAcceptInsecureCerts(true);
        options.setExperimentalOption("w3c", true);

        return options;
    }
}
