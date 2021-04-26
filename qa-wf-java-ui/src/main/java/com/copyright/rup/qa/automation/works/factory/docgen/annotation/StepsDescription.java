package com.copyright.rup.qa.automation.works.factory.docgen.annotation;

import java.lang.annotation.*;

/**
 * Annotation for class description.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 14, 2021
 *
 * @author Dzmitry Padskokau
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface StepsDescription {
    SubMenu[] value() default {};
}
