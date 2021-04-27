package com.copyright.rup.qa.automation.works.factory.docgen.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Dto to represent a parsed class.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 15, 2021
 *
 * @author Dzmitry Padskokau
 */
public class ParsedClass {

    private List<String> menuHierarchy;
    private List<Step> steps;

    /**
     * Constructor.
     */
    public ParsedClass() {
    }

    /**
     * Constructor.
     *
     * @param menuHierarchy menu hierarchy
     * @param steps list of steps
     */
    public ParsedClass(List<String> menuHierarchy,
            List<Step> steps) {
        this.menuHierarchy = menuHierarchy;
        this.steps = steps;
    }

    public List<String> getMenuHierarchy() {
        return menuHierarchy;
    }

    public void setMenuHierarchy(List<String> menuHierarchy) {
        this.menuHierarchy = menuHierarchy;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParsedClass that = (ParsedClass) o;

        return new EqualsBuilder()
                .append(menuHierarchy, that.menuHierarchy)
                .append(steps, that.steps)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(menuHierarchy)
                .append(steps)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("menuHierarchy", menuHierarchy)
                .append("steps", steps)
                .toString();
    }
}
