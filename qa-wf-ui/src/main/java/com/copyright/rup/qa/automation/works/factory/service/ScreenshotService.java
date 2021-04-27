package com.copyright.rup.qa.automation.works.factory.service;

import static com.codeborne.selenide.Selenide.executeJavaScript;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;
import com.copyright.rup.qa.common.ui.support.DriverRunner;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 * Provides a possibility to create screenshots.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Sep 18, 2020
 *
 * @author Aliaksandr Yushkevich
 */
@Component
public class ScreenshotService {
    private static final Logger LOGGER = RupLogUtils.getLogger();

    /**
     * Creates screenshot and moves it to the Report Portal.
     *
     * @param screenshotName name of the screenshot
     */
    public void takeScreenshot(String screenshotName) {
        if (DriverRunner.hasWebDriverStarted()) {
            logScreenshotToReportPortal(createScrollableScreenshot(normalizeFileName(screenshotName)));
        }
    }

    /**
     * Cleans screenshot directory.
     */
    public void cleanScreenshots() {
        File screenshotFolder = new File(TestParameters.SCREENSHOT_PATH);
        if (screenshotFolder.exists()) {
            try {
                FileUtils.cleanDirectory(screenshotFolder);
                LOGGER.info("Screenshots folder [{}] has been cleaned", screenshotFolder.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.info("Error on cleaning screenshots folder", e);
            }
        }
    }

    private File createScrollableScreenshot(String screenshotName) {
        WebDriver webDriver = DriverRunner.getWebDriver();
        float devicePixelRatio = getDevicePixelRatio();
        Screenshot myScreenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportRetina(100, 0, 0, devicePixelRatio))
                .takeScreenshot(webDriver);
        File result = getScreenshotFile(screenshotName);
        try {
            ImageIO.write(myScreenshot.getImage(), "PNG", result);
        } catch (IOException e) {
            LOGGER.info("Error on creating screenshot file", e);
        }

        return result;
    }

    private float getDevicePixelRatio() {
        return Float.valueOf(executeJavaScript("return window.devicePixelRatio").toString());
    }

    private void logScreenshotToReportPortal(File screenshot) {
        LOGGER.info("RP_MESSAGE#FILE#{}#{}", screenshot.getAbsolutePath(), "screenshot");
    }

    private String normalizeFileName(String name) {
        String trimmedString = name.replaceAll("[\\\\/:*?\\\"<>|\\x00-\\x1e]", "");
        if (trimmedString.length() > 128) {
            return trimmedString.substring(0, 128) + "...";
        }
        return trimmedString;
    }

    private File getScreenshotFile(String screenshotName) {
        File screenshotFile = new File(TestParameters.SCREENSHOT_PATH, screenshotName + ".png");
        File folder = screenshotFile.getParentFile();
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                LOGGER.info("Folder for screenshots [{}] has been successfully created", folder.getAbsolutePath());
            } else {
                LOGGER.info("Unable to create folder for screenshots [{}]", folder.getAbsolutePath());
            }
        } else {
            LOGGER.info("Folder [{}] for screenshots already exists", folder.getAbsolutePath());
        }
        return screenshotFile;
    }
}
