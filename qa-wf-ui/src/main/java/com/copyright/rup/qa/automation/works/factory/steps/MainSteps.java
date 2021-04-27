package com.copyright.rup.qa.automation.works.factory.steps;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.docgen.annotation.StepsDescription;
import com.copyright.rup.qa.automation.works.factory.docgen.annotation.SubMenu;
import com.copyright.rup.qa.automation.works.factory.runner.ExecutionContext;
import com.copyright.rup.qa.automation.works.factory.runner.ExecutionScopeRuntime;
import com.copyright.rup.qa.automation.works.factory.service.ScreenshotService;
import com.copyright.rup.qa.automation.works.factory.utils.BrowserType;
import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;
import com.copyright.rup.qa.automation.works.factory.webdriver.ChromeDriverProvider;
import com.copyright.rup.qa.common.ui.support.DriverRunner;
import com.codeborne.selenide.Configuration;
import com.google.common.collect.ImmutableMap;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.ScenarioType;
import org.jbehave.core.annotations.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * Prepare web drivers.
 */
@Component
@StepsDescription({
        @SubMenu("General")
})
public class MainSteps {
    private static final Logger LOGGER = RupLogUtils.getLogger();
    private static final String NOT_SUPPORTED = "'%s' browser is not supported";

    @Autowired
    private ScreenshotService screenshotService;

    private BrowserType browser;

    private Map<BrowserType, String> webDriverProviders = ImmutableMap.of(
            BrowserType.CHROME, ChromeDriverProvider.class.getCanonicalName()
    );

    public void setBrowser(BrowserType browser) {
        this.browser = browser;
    }

    /**
     * Cleans screenshots directory.
     */
    @BeforeStories
    public void beforeStories() {
        screenshotService.cleanScreenshots();
    }

    /**
     * Set up.
     */
    @BeforeStory
    public void setUp() {
        ExecutionContext.getStoryRuntime().setUp();
        Configuration.reportsFolder = TestParameters.SCREENSHOT_PATH;
        if (!DriverRunner.hasWebDriverStarted()) {
            LOGGER.info("Create new web browser.");
            Configuration.browser = Optional.of(webDriverProviders.get(this.browser)).orElseThrow(() ->
                    new IllegalArgumentException(String.format(NOT_SUPPORTED, TestParameters.BROWSER)));
        }
        DriverRunner.clearBrowserCache();
    }

    /**
     * Before scenario.
     */
    @BeforeScenario
    public void beforeScenario() {
        ExecutionContext.getScenarioRuntime().setUp();
    }

    /**
     * Tear down.
     */
    @AfterStory
    public void tearDown() {
        ExecutionScopeRuntime storyRuntime = ExecutionContext.getStoryRuntime();
        storyRuntime.tearDown();
        storyRuntime.getContext().clear();
        if (DriverRunner.hasWebDriverStarted()) {
            LOGGER.info("Close web driver.");
            DriverRunner.closeWebDriver();
        }
    }

    /**
     * After scenario.
     */
    @AfterScenario(uponType = ScenarioType.ANY)
    public void afterScenario() {
        ExecutionContext.getScenarioRuntime().tearDown();
    }

    /**
     * Adds a rules to close browser at the end of test.
     */
    @When("user sets to close browser after verification")
    public void addHookToCloseDriver() {
        ExecutionContext.getScenarioRuntime().addAfterHook(() -> {
            DriverRunner.closeWebDriver();
        });
    }
}
