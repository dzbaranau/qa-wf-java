package com.copyright.rup.qa.automation.works.factory.reporter;
import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.pubportal.special.request.service.ScreenshotService;

import com.epam.reportportal.jbehave.ReportPortalStoryReporter;
import org.jbehave.core.model.Story;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Custom reporter for Report Portal.
 * <p>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 7/27/2018
 *
 * @author Kiryl_Milavidau
 */
@Component
public class CustomStoryReporter extends ReportPortalStoryReporter {

    private static final Logger LOGGER = RupLogUtils.getLogger();

    @Autowired
    private ScreenshotService screenshotService;

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        clearEmptySteps(story, givenStory);
    }

    @Override
    public void afterStory(boolean givenStory) {
        super.afterStory(givenStory);
    }

    @Override
    public void failed(String step, Throwable cause) {
        screenshotService.takeScreenshot(step);
        super.failed(step, cause);
        LOGGER.info("Failed: {} because {}", step, cause);
    }

    private void clearEmptySteps(Story story, boolean givenStory) {
        if (!"BeforeStories".equals(story.getPath()) && !"AfterStories".equals(story.getPath())) {
            super.beforeStory(story, givenStory);
        }
    }
}
