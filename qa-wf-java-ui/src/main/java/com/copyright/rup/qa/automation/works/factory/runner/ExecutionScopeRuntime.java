package com.copyright.rup.qa.automation.works.factory.runner;

import com.copyright.rup.common.logging.RupLogUtils;

import org.slf4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Execution Scope runtime object.
 *
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: Aug 15, 2018
 *
 * @author Kiryl Milavidau
 */
public final class ExecutionScopeRuntime {
    private static final Logger LOGGER = RupLogUtils.getLogger();
    private final Deque<Runnable> beforeHooks = new ArrayDeque<>();
    private final Deque<Runnable> afterHooks = new ArrayDeque<>();

    /** Request id param. */
    public static final String SELECTED_REQUEST_ID_KEY = "request_id";

    private final ExecutionScopeType type;
    private SharedContext ctx = new SharedContext();

    /**
     * Ctor.
     *
     * @param scope scope.
     */
    public ExecutionScopeRuntime(ExecutionScopeType scope) {
        this.type = scope;
    }

    /**
     * Sets up the execution context. Client assigns id and name.
     */
    public void setUp() {
        while (!beforeHooks.isEmpty()) {
            LOGGER.debug("Run Before hooks");
            beforeHooks.poll().run();
        }
    }

    /**
     * Tear down the test, performs necessary cleanup etc.
     */
    public void tearDown() {
        LOGGER.info("Cleanning up the context");
        while (!afterHooks.isEmpty()) {
            LOGGER.debug("Run After hooks");
            afterHooks.poll().run();
        }
    }

    /**
     * Adds hook for before event.
     *
     * @param hook hook to be attached.
     */
    public void addBeforeHook(Runnable hook) {
        beforeHooks.add(hook);
    }

    /**
     * Add hook for before event and moves it to the beginning of queue.
     *
     * @param hook hook to be attached.
     */
    public void addFirstBeforeHook(Runnable hook) {
        beforeHooks.addFirst(hook);
    }

    /**
     * Adds hook for after event.
     *
     * @param hook hook to be attached.
     */
    public void addAfterHook(Runnable hook) {
        afterHooks.add(hook);
    }

    /**
     * Add hook for after event and moves it to the beginning of queue.
     *
     * @param hook hook to be attached.
     */
    public void addFirstAfterHook(Runnable hook) {
        afterHooks.addFirst(hook);
    }

    /**
     * Retrieves shared context (shared parameters).
     *
     * @return {@link SharedContext} shared context.
     */
    public SharedContext getContext() {
        return ctx;
    }

    /**
     * Execution scope.
     */
    public enum ExecutionScopeType {
        /** Test case level, in JBehave corresponds to Scenario. */
        SCENARIO("scenario"),
        /** Test case level, in JBehave corresponds to Story. */
        STORY("story");

        private String name;

        /**
         * Ctor.
         * @param name name
         */
        ExecutionScopeType(final String name) {
            this.name = name;
        }

        /**
         * Gets name of scope (mainly for log purposes).
         *
         * @return name of scope.
         */
        public String getName() {
            return name;
        }
    }
}

