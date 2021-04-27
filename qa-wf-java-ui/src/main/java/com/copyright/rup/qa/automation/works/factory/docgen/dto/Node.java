package com.copyright.rup.qa.automation.works.factory.docgen.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * The node element of the tree structure of steps.
 * <p/>
 * Copyright (C) 2021 copyright.com
 * <p/>
 * Date: Jan 14, 2021
 *
 * @author Dzmitry Padskokau
 */
public class Node {

    private String subMenu;
    private List<Step> steps = new ArrayList<>();
    private Node parent;
    private Set<Node> children = new HashSet<>();

    /**
     * Constructor.
     *
     * @param subMenu name of paragraph
     */
    public Node(String subMenu) {
        this.subMenu = subMenu;
    }

    /**
     * Constructor.
     *
     * @param subMenu name of paragraph
     * @param steps   list of steps for paragraph
     */
    public Node(String subMenu, List<Step> steps) {
        this.subMenu = subMenu;
        this.steps = steps;
    }

    /**
     * Gets or creates child element by sub menu name.
     *
     * @param paragraph name of paragraph
     * @return child as node
     */
    public Node getOrCreateChildByMenu(String paragraph) {
        Optional<Node> child = children.stream()
                .filter(node -> node.getSubMenu().equals(paragraph))
                .findFirst();
        if (child.isPresent()) {
            return child.get();
        } else {
            Node newSubMenu = new Node(paragraph);
            newSubMenu.setParent(this);
            children.add(newSubMenu);
            return newSubMenu;
        }
    }

    public String getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(String subMenu) {
        this.subMenu = subMenu;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Set<Node> getChildren() {
        return children;
    }

    public void setChildren(Set<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node node = (Node) o;

        return new EqualsBuilder()
                .append(subMenu, node.subMenu)
                .append(steps, node.steps)
                .append(parent, node.parent)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(subMenu)
                .append(steps)
                .append(parent)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("subMenu", subMenu)
                .append("steps", steps)
                .append("parent", parent)
                .toString();
    }
}
