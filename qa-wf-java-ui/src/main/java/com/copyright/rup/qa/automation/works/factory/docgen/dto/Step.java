package com.copyright.rup.qa.automation.works.factory.docgen.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Dto to represent a step.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 12, 2021
 *
 * @author Dzmitry Padskokau
 */
public class Step {
    private StepType type;
    private String definition;
    private String description;
    private List<Parameter> parameters;

    /**
     * Constructor.
     */
    public Step() {
    }

    /**
     * Constructor.
     *
     * @param type        type of step
     * @param definition  definition of step from jbehave annotation
     * @param description step description
     * @param parameters  list of step parameters
     */
    public Step(StepType type, String definition, String description,
            List<Parameter> parameters) {
        this.type = type;
        this.definition = definition;
        this.description = description;
        this.parameters = parameters;
    }

    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(
            List<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Step step = (Step) o;

        return new EqualsBuilder()
                .append(type, step.type)
                .append(definition, step.definition)
                .append(description, step.description)
                .append(parameters, step.parameters)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(type)
                .append(definition)
                .append(description)
                .append(parameters)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("definition", definition)
                .append("description", description)
                .append("parameters", parameters)
                .toString();
    }
}
