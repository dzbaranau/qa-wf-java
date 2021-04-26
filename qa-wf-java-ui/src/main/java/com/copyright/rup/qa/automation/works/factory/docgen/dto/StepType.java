package com.copyright.rup.qa.automation.works.factory.docgen.dto;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents step types.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 16, 2021
 *
 * @author Dzmitry Padskokau
 */
public enum StepType {
    /**
     * Given step type.
     */
    GIVEN("Given"),
    /**
     * When step type.
     */
    WHEN("When"),
    /**
     * Then step type.
     */
    THEN("Then"),
    /**
     * Alias step type.
     */
    ALIAS("Alias"),
    /**
     * Aliases step type.
     */
    ALIASES("Aliases");

    private String name;

    private StepType(String name) {
        this.name = name;
    }

    /**
     * Name of step in Jbehave notation.
     *
     * @return name of step.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets step type by name.
     *
     * @param name name of step
     * @return step type
     */
    public static Optional<StepType> valueOfByName(String name) {
        return Stream.of(StepType.values())
                .filter(type -> type.getName().equals(name))
                .findFirst();
    }
}
