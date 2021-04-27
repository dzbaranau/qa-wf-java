package com.copyright.rup.qa.automation.works.factory.runner;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Shared context - a common place for parts of test execution to share the context test is executed in.
 * So if prereq creates and entity, it can put entity id in this context and test can retrieve it later and use
 * for assertion or lookup.
 * <p/>
 * Copyright (C) 2018 copyright.com
 * <p/>
 * Date: Aug 15, 2018
 *
 * @author Kiryl Milavidau
 */
public class SharedContext {
    private final Map<String, Object> ctx = Maps.newHashMap();

    /**
     * Gets parameter as string. Note that parameters in context are of object type. This method gets shared parameter
     * only if it is string-typed or null. Use getParamObj to get object parameter.
     *
     * @param name parameter name.
     * @return string parameter (can be null).
     */
    public String getParam(String name) {
        Object obj = getParamObj(name);
        if(obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        throw new ClassCastException("Key " + name + " is of class " + obj.getClass().toString());
    }

    /**
     * Puts parameter into shared context.
     *
     * @param name  name of parameter.
     * @param value value of parameter.
     */
    public void putParam(String name, Object value) {
        ctx.put(name, value);
    }

    /**
     * Gets parameter value (assuming parameter is generic object).
     *
     * @param name parameter name
     * @param <T>  generic parameter
     * @return parameter value
     */
    @SuppressWarnings({"unchecked"})
    public <T> T getParamObj(String name) {
        return (T) ctx.get(name);
    }

    /** Clears the context. */
    public void clear() {
        ctx.clear();
    }
}
