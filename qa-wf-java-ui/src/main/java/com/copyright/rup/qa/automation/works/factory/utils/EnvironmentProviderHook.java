package com.copyright.rup.qa.automation.works.factory.utils;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.google.common.base.Preconditions;

/**
 * Provisions qaa.environment into rup.environment.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: 19/04/18
 *
 * @author Dzmitry Dziokin
 */
public final class EnvironmentProviderHook {
    private static final String ENV_PROP = "qaa.environment";
    private static final String RUP_ENV_PROP = "rup.environment";

    private EnvironmentProviderHook() {
        // no ctor
    }

    /**
     * Sets up environment (if it is not yet set) to local env.
     */
    public static void ensureEnvironmentSet() {
        final String environment = defaultIfEmpty(System.getProperty(ENV_PROP), System.getenv(ENV_PROP));
        Preconditions.checkArgument(isNotEmpty(environment),
                "qaa.environment or rup.environment java properties must be set");
        System.setProperty(RUP_ENV_PROP, environment.toLowerCase());
    }
}
