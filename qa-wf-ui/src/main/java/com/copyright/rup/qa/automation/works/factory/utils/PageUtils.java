package com.copyright.rup.qa.automation.works.factory.utils;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.common.ui.support.DriverRunner;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Common page utils.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 04/10/18
 *
 * @author Dzmitry Dziokin
 */
public final class PageUtils {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    private PageUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Refresh a browser page.
     */
    public static void pageRefresh() {
        LOGGER.info("Refreshing the page");
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("location.reload()");
        PageUtils.waitForBlockUIDisappear(1);
    }

    /**
     * Go back in a browser.
     */
    public static void back() {
        LOGGER.info("Go to the previous page.");
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("window.history.back()");
        PageUtils.waitForBlockUIDisappear(10);
    }

    /**
     * Go forward in a browser.
     */
    public static void forward() {
        LOGGER.info("Go to the previous page.");
        JavascriptExecutor js = (JavascriptExecutor) WebDriverRunner.getWebDriver();
        js.executeScript("window.history.forward()");
        PageUtils.waitForBlockUIDisappear(1);
    }

    /**
     * Moves focus to the tab which was opened after the action.
     *
     * @param action action which produces new tab
     */
    public static void switchToTab(IAction action) {
        WebDriver webDriver = DriverRunner.getWebDriver();
        Set<String> oldWindows = webDriver.getWindowHandles();
        action.doAction();
        Optional<String> optionalHandle = webDriver.getWindowHandles().stream()
                .filter(windowHandle -> !oldWindows.contains(windowHandle))
                .findFirst();
        if (optionalHandle.isPresent()) {
            webDriver.close();
            webDriver.switchTo().window(optionalHandle.get());
        }
    }

    /**
     * Switch to another browser tab.
     *
     * @param tab the tab index
     * @param order tab order (0 is the left or the right one)
     */
    public static void switchToBrowserTab(int tab, TabOrder order) {
        if (TabOrder.FROM_LEFT == order) {
            Selenide.switchTo().window(tab);
        } else if (TabOrder.FROM_RIGHT == order) {
            ArrayList<String> tabs = new ArrayList<>(WebDriverRunner.getWebDriver().getWindowHandles());
            Selenide.switchTo().window(tabs.size() - 1 - tab);
        }
    }

    /**
     * Waits for page is fully loaded.
     *
     * @param seconds number of seconds to wait.
     */
    public static void waitForBlockUIDisappear(int seconds) {
        waitWhileBlockDisappear(".blockUI.blockOverlay", seconds);
        waitWhileBlockDisappear(".blockUI.blockMsg.BlockElement", seconds);
    }

    /**
     * Waits for page is not blocked any more.
     *
     * @param seconds seconds
     */
    public static void waitForUInotBlocked(int seconds) {
        waitForUInotBlocked(".blockUI.blockOverlay", seconds);
        waitForUInotBlocked(".blockUI.blockMsg.BlockElement", seconds);
    }

    /**
     * Gets title of open web page.
     *
     * @return title of open web page
     */
    public static String getPageTabTitle() {
        return WebDriverRunner.getWebDriver().getTitle();
    }

    /**
     * Gets URL of currently opened page.
     *
     * @return URL
     */
    public static String getPageUrl() {
        return WebDriverRunner.getWebDriver().getCurrentUrl();
    }

    /**
     * Is input read only boolean.
     *
     * @param element the element
     * @return the boolean
     */
    public static boolean isInputReadOnly(SelenideElement element) {
        return element.attr("Class").contains("disabled") ||
                element.attr("Class").contains("readonly") ||
                element.attr("Class").contains("blocker") ||
                !element.isEnabled() ||
                Objects.nonNull(element.attr("readonly"));
    }

    /**
     * Closes current tab.
     */
    public static void closeCurrentTab() {
        WebDriverRunner.getWebDriver().close();
        switchToBrowserTab(0, TabOrder.FROM_RIGHT);
    }

    /**
     * Opens url in blank page with no content.
     *
     * @param url url
     */
    public static void openUrlInBlankPage(String url) {
        executeJavaScript("window.location.href='about:blank'");
        $("html").shouldHave(text(""));
        LOGGER.debug("Opening " + url);
        open(url);
    }

    private static void waitForUInotBlocked(String cssSelector, int seconds) {
        try {
            new FluentWait<>(seconds).withTimeout(Duration.ofSeconds(seconds))
                    .until(f -> !($(cssSelector).exists()));
        } catch (TimeoutException e) {
            LOGGER.info("Element: [{}] is still displayed", cssSelector);
        }
    }

    private static void waitWhileBlockDisappear(String cssSelector, int seconds) {
        try {
            new FluentWait<>(0).withTimeout(Duration.ofMillis(1000))
                    .until(f -> ($(cssSelector).exists()));
        } catch (TimeoutException e) {
            LOGGER.info("Element: {} was not found", cssSelector);
        }
        try {
            new FluentWait<>(seconds).withTimeout(Duration.ofSeconds(seconds))
                    .until(f -> !($(cssSelector).exists()));
        } catch (TimeoutException e) {
            LOGGER.info("Element: {} is still displayed", cssSelector);
        }
    }

    /**
     * retry runnable while is executed many time in case of exception.
     *
     * @param runnable runnable to execute.
     * @param maxRetries maximum number or retries.
     * @param sleepMilliseconds sleep in milliseconds between retries.
     */
    public static void retry(Runnable runnable, int maxRetries, long sleepMilliseconds) {
        retry(()-> {
            runnable.run();
            return Void.class;
        }, maxRetries, sleepMilliseconds);
    }

    /**
     * retry method while some supplier is executed.
     *
     * @param <T> type to return by the supplier
     * @param supplier supplier to execute.
     * @param maxRetries maximum number or retries.
     * @param sleepMilliseconds sleep in milliseconds between retries.
     * @return object from supplier
     */
    public static <T> T retry(Supplier<T> supplier, int maxRetries, long sleepMilliseconds) {
        int count = 0;
        while (true) {
            count++;
            try {
                return supplier.get();
            } catch (Throwable e) {
                if (count >= maxRetries) {
                    throw e;
                }
                sleep(sleepMilliseconds);
                LOGGER.warn("Error in retry", e);
            }
        }
    }

    /**
     * Order of tabs to close or switch.
     */
    public enum TabOrder {
        /**
         * Order from left to right (0-based).
         */
        FROM_LEFT,
        /**
         * Order from right to left (0-based).
         */
        FROM_RIGHT
    }
}
