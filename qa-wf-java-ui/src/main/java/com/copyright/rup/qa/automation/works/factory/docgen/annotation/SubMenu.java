package com.copyright.rup.qa.automation.works.factory.docgen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents menu element.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 14, 2021
 *
 * @author Dzmitry Padskokau
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface SubMenu {
    String value() default "";
}
