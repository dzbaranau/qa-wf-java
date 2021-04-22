package com.copyright.rup.qa.automation.works.factory.runner;

/**
 * Global execution context.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: Aug 15, 2018
 *
 * @author Kiryl Milavidau
 */
public final class ExecutionContext {
    private static final ThreadLocal<ExecutionContext> CTX = ThreadLocal.withInitial(ExecutionContext::new);

    private final ExecutionScopeRuntime scenarioRuntime = new ExecutionScopeRuntime(
            ExecutionScopeRuntime.ExecutionScopeType.SCENARIO);
    private final ExecutionScopeRuntime storyRuntime = new ExecutionScopeRuntime(
            ExecutionScopeRuntime.ExecutionScopeType.STORY);

    private ExecutionContext() {
        //noop
    }

    /**
     * Gets instance of this ExecutionContext. ExecutionContext implements thread-local singleton pattern, so
     * for each thread separate instance will be created, this helps to run tests in multiple threads.
     *
     * @return {@link ExecutionContext} instance.
     */
    private static ExecutionContext get() {
        return CTX.get();
    }

    /**
     * Gets scenario runtime.
     *
     * @return scenario runtime.
     */
    public static ExecutionScopeRuntime getScenarioRuntime() {
        return get().scenarioRuntime;
    }

    public static ExecutionScopeRuntime getStoryRuntime() {
        return get().storyRuntime;
    }
}
