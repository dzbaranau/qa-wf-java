package com.copyright.rup.qa.automation.works.factory.service;

import com.copyright.rup.common.config.RupPropertiesConfigurerExt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service to work with placeholders according to values from the properties file.
 * <p/>
 * Copyright (C) 2019 copyright.com
 * <p/>
 * Date: Mar 21, 2019
 *
 * @author Aliaksandr Yushkevich
 */
@Component
public class PlaceholderService {

    @Autowired
    private RupPropertiesConfigurerExt propertiesConfigurer;

    /**
     * Resolves property according to the placeholders and returns placeholder back if value not found.
     *
     * @param placeholder placeholder for resolving
     * @return value or placeholder if resolving is not possible
     */
    public String resolveProperty(String placeholder) {
        String value = propertiesConfigurer.resolveProperty(placeholder);
        return value != null ? value : placeholder;
    }
}
