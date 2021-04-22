package com.copyright.rup.qa.automation.works.factory.utils;

/**
 * Functional interface to do an action without parameters and result.
 * <p/>
 * Copyright (C) 2020 copyright.com
 * <p/>
 * Date: Feb 03, 2020
 *
 * @author Aliaksandr Yushkevich
 */
@FunctionalInterface
public interface IAction {
    /**
     * Does action.
     */
    void doAction();
}
