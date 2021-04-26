package com.copyright.rup.qa.automation.works.factory.runner;

import static com.copyright.rup.qa.automation.works.factory.utils.TestParameters.META_FILTERS;
import static java.util.stream.Collectors.toList;

import com.copyright.rup.common.logging.RupLogUtils;
import com.copyright.rup.qa.automation.works.factory.reporter.CustomStoryReporter;
import com.copyright.rup.qa.automation.works.factory.steps.MainSteps;
import com.copyright.rup.qa.automation.works.factory.utils.BrowserType;
import com.copyright.rup.qa.automation.works.factory.utils.EnvironmentProviderHook;
import com.copyright.rup.qa.automation.works.factory.utils.TestParameters;

import com.epam.reportportal.jbehave.ReportPortalViewGenerator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.FailingUponPendingStep;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.io.UnderscoredToCapitalized;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.StoryMaps;
import org.jbehave.core.model.TableTransformers;
import org.jbehave.core.reporters.ConcurrentStoryReporter;
import org.jbehave.core.reporters.DelegatingStoryReporter;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.FreemarkerViewGenerator;
import org.jbehave.core.reporters.ReportsCount;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.reporters.ViewGenerator;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterControls;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.junit.Assert;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Story runner.
 * <p>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 7/13/2018
 *
 * @author Kiryl_Milavidau
 * @author Dzmitry Dziokin
 */
@RunWith(Parameterized.class)
//@ContextConfiguration(locations = "classpath:/context.xml")
public class StoryRunner extends JUnitStories {

    private static final Logger LOGGER;

    private static final int THREADS_COUNT = 1;
    private static final String COMMA_DELIMITER = ",";
    private static final String MAX_TIMEOUT_STORY_IN_SECS = "6000";

    private TestContextManager testContextManager = new TestContextManager(this.getClass());

    @Autowired
    private ApplicationContext context;
    @Autowired
    private CustomStoryReporter reporter;
    @Autowired
    private MainSteps mainSteps;

    static {
        System.setProperty("rp.tags", new StringBuilder(TestParameters.SYSTEM_ENVIRONMENT).append(';').
                append(TestParameters.META_FILTERS.replaceAll("(,| )+", ";")).append(';').toString());
        EnvironmentProviderHook.ensureEnvironmentSet();
        LOGGER = RupLogUtils.getLogger();
    }

    /**
     * Constructor.
     *
     * @param browser browser.
     */
    public StoryRunner(BrowserType browser) {
        try {
            this.testContextManager.prepareTestInstance(this);
            //            reporter.setBrowser(browser);
            mainSteps.setBrowser(browser);
        } catch (Exception e) {
            Assert.fail("Cannot init runner instance due to exception " + e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Tests are run for {} profiles", Arrays.toString(context.getEnvironment().getActiveProfiles()));
        super.run();
    }

    /**
     * Browser data provider.
     *
     * @return collection of browser names.
     */
    @Parameterized.Parameters
    public static Collection<BrowserType> browsers() {
        return BooleanUtils.toBoolean(TestParameters.CROSS_BROWSER) ?
                Stream.of(BrowserType.values()).collect(toList()) : Collections.singletonList(TestParameters.BROWSER);
    }

    /**
     * Main.
     *
     * @param args arguments.
     */
    public static void main(String[] args) {
        Request request = Request.aClass(StoryRunner.class);
        JUnitCore jUnitCore = new JUnitCore();
        try {
            jUnitCore.run(request); // just for simplicity, must be adjusted
        } finally {
            System.exit(0);
        }
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), context);
    }

    @Override
    public Configuration configuration() {
        TableTransformers transformers = new TableTransformers();
        return new MostUsefulConfiguration()
                .useParameterControls(getParameterControls())
                .usePendingStepStrategy(new FailingUponPendingStep())
                .useParameterConverters(new ParameterConverters(transformers))
//                    .addConverters(new RecordParameterConverter(transformers)))
                .useStoryLoader(new LoadFromClasspath(getClass()))
                .useStoryReporterBuilder(getStoryReporterBuilder())
                .useViewGenerator(new DelegatingReportPortalViewGenerator(new FreemarkerViewGenerator(
                        new UnderscoredToCapitalized(), FreemarkerViewGenerator.class, StandardCharsets.UTF_8)));
    }

    @Override
    public Embedder configuredEmbedder() {
        Embedder embedder = super.configuredEmbedder();
        embedder.useMetaFilters(getMetaFilters());
        embedder.embedderControls()
                .useStoryTimeouts(MAX_TIMEOUT_STORY_IN_SECS)
                .doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true)
                .useThreads(THREADS_COUNT)
                .generateViewAfterStories();

        return embedder;
    }

    @Override
    public List<String> storyPaths() {
        List<String> filesToRun = new StoryFinder()
                .findPaths(CodeLocations.codeLocationFromClass(this.getClass()), getStoriesPath(), null);
        LOGGER.info("Found stories to run: {}", filesToRun);
        return filesToRun;
    }

    /**
     * Override this method to specify the location of stories if default location does not suit.
     *
     * @return path to look for stories (can have ant wildcards inside).
     */
    private String getStoriesPath() {
        String qaaStory = System.getProperty("qaa.story", "**/*");
        if (!StringUtils.containsAny(qaaStory, '/', '\\')) {
            qaaStory = "**/" + qaaStory;
        }
        return "stories/" + qaaStory + ".story";
    }

    private StoryReporterBuilder getStoryReporterBuilder() {
        //Replace with RP in future
        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        viewResources.put("encoding", "UTF-8");
        return new CustomStoryReporterBuilder(reporter)
                .withRelativeDirectory("../build/jbehave")
                .withViewResources(viewResources)
                .withFormats(Format.STATS, Format.HTML_TEMPLATE, Format.XML)
                .withPathResolver(new FilePrintStreamFactory.ResolveToPackagedName());
    }

    private ParameterControls getParameterControls() {
        return new ParameterControls().useDelimiterNamedParameters(true);
    }

    private List<String> getMetaFilters() {
        return BooleanUtils.toBoolean(TestParameters.CROSS_BROWSER) ?
                Collections.singletonList(TestParameters.MAKE_SCREENSHOT_META) :
                Stream.of(META_FILTERS)
                        .flatMap(Pattern.compile(COMMA_DELIMITER)::splitAsStream)
                        .collect(toList());
    }

    /**
     * Custom story reporter builder to register listener and be able to use custom reporter.
     */
    private static class CustomStoryReporterBuilder extends StoryReporterBuilder {

        private StoryReporter reporter;

        /**
         * Creates builder with main reporter.
         *
         * @param reporter reporter.
         */
        CustomStoryReporterBuilder(StoryReporter reporter) {
            this.reporter = reporter;
        }

        @Override
        public StoryReporter build(String storyPath) {
            Map<org.jbehave.core.reporters.Format, StoryReporter> delegates = new HashMap<>();
            for (org.jbehave.core.reporters.Format format : formats()) {
                delegates.put(format, reporterFor(storyPath, format));
            }
            DelegatingStoryReporter delegate = new DelegatingStoryReporter(delegates.values());
            return new ConcurrentStoryReporter(reporter, delegate, multiThreading());
        }
    }

    /**
     * Custom implementation of {@link ReportPortalViewGenerator} to allow delegate calls to another view generator.
     * Basically it's done to allow usage of several view generators.
     */
    private static class DelegatingReportPortalViewGenerator extends ReportPortalViewGenerator {

        private ViewGenerator delegate;

        /**
         * Constructor.
         *
         * @param delegate delegate.,
         */
        public DelegatingReportPortalViewGenerator(ViewGenerator delegate) {
            this.delegate = delegate;
        }

        @Override
        public void generateMapsView(File outputDirectory, StoryMaps storyMaps, Properties viewResources) {
            super.generateMapsView(outputDirectory, storyMaps, viewResources);
            delegate.generateMapsView(outputDirectory, storyMaps, viewResources);
        }

        @Override
        public void generateReportsView(File outputDirectory, List<String> formats, Properties viewResources) {
            super.generateReportsView(outputDirectory, formats, viewResources);
            delegate.generateReportsView(outputDirectory, formats, viewResources);
        }

        @Override
        public ReportsCount getReportsCount() {
            return super.getReportsCount();
        }

        @Override
        public Properties defaultViewProperties() {
            return delegate.defaultViewProperties();
        }
    }
}
