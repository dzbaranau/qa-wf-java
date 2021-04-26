package com.copyright.rup.qa.automation.works.factory.docgen.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Dto to represent a javadoc method parameter.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 12, 2021
 *
 * @author Dzmitry Padskokau
 */
public class Parameter {

    private String name;
    private String description;

    /**
     * Constructor.
     */
    public Parameter() {
    }

    /**
     * Constructor.
     *
     * @param name        parameter name
     * @param description parameter description
     */
    public Parameter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Parameter parameter = (Parameter) o;

        return new EqualsBuilder()
                .append(name, parameter.name)
                .append(description, parameter.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(description)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("description", description)
                .toString();
    }
}
