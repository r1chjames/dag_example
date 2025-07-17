package com.r1chjames;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class Graph {

    @Getter
    protected Set<Node> nodes = new HashSet<>();
    @Getter
    protected Set<Link> links = new HashSet<>();

    public void addNode(final Node node) {
        nodes.add(node);
    }

    public void addLink(final Link link) {
        if (!nodes.contains(link.getSource()) || !nodes.contains(link.getDestination())) {
            throw new IllegalArgumentException("Nodes must exist before creating a link");
        }

        if (addingLinkResultsInCycle(link)) {
            throw new IllegalArgumentException("The link candidate will result in a cycle");
        }
        links.add(link);
    }

    private boolean addingLinkResultsInCycle(final Link link) {
        if (link.getSource().equals(link.getDestination())) return true;

        return getDescendantNodes(link.getDestination()).contains(link.getSource());
    }

    public boolean hasLink(final Node source, final Node destination) {
        return links.contains(new Link(source, destination));
    }

    public Set<Node> getChildNodes(final Node node) {
        Set<Node> nodesToReturn = new HashSet<>();
        return getLinkedNodes(node, nodesToReturn);
    }

    private Set<Node> getLinkedNodes(final Node node, Set<Node> nodesToReturn) {
        links
            .forEach(l -> {
                if(l.getSource().equals(node)) {
                    nodesToReturn.add(l.getDestination());
                }
            });
        return nodesToReturn;
    }

    public Set<Node> getDescendantNodes(final Node node) {
        Set<Node> nodesToReturn = new HashSet<>();
        return getDescendantNodes(node, nodesToReturn);
    }

    private Set<Node> getDescendantNodes(final Node node, Set<Node> nodesToReturn) {
        var childNodes = getChildNodes(node);
        childNodes
            .forEach(n -> {
                nodesToReturn.add(n);
                if (!getChildNodes(n).isEmpty()) {
                    getDescendantNodes(n, nodesToReturn);
                }
            });
        return nodesToReturn;
    }

    public Set<Node> getParentNodes(final Node node) {
        Set<Node> nodesToReturn = new HashSet<>();
        return getParentNodes(node, nodesToReturn);
    }

    private Set<Node> getParentNodes(final Node node, Set<Node> nodesToReturn) {
        links
            .forEach(l -> {
                if(l.getDestination().equals(node)) {
                    nodesToReturn.add(l.getSource());
                }
            });
        return nodesToReturn;
    }

    public Set<Node> getAncestorsNode(final Node node) {
        Set<Node> nodesToReturn = new HashSet<>();
        return getAncestorsNode(node, nodesToReturn);
    }

    private Set<Node> getAncestorsNode(final Node node, Set<Node> nodesToReturn) {
        var parentNodes = getParentNodes(node);
        parentNodes.forEach(n -> {
            nodesToReturn.add(n);
            if(!getParentNodes(n).isEmpty()) {
                getAncestorsNode(n, nodesToReturn);
            }
        });
        return nodesToReturn;
    }
}
