package com.r1chjames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

public class GraphTests extends Graph{

    private Graph graph;

    @BeforeEach
    void before() {
        this.graph = new Graph();
    }

    @Test
    void addNode() {
        var nodeToAdd = new Node("A");
        graph.addNode(nodeToAdd);
        assertThat(graph.getNodes()).isEqualTo(Set.of(nodeToAdd));
    }

    @Test
    void addDuplicateNodes() {
        var nodeToAdd = new Node("A");
        graph.addNode(nodeToAdd);
        graph.addNode(nodeToAdd);
        assertThat(graph.getNodes()).isEqualTo(Set.of(nodeToAdd));
    }

    @Test
    void addLinkWhenNodesDontExist() {
        var linkToAdd = new Link(new Node("A"), new Node("B"));
        assertThatThrownBy(() -> graph.addLink(linkToAdd)).isInstanceOf(IllegalArgumentException.class).hasMessage("Nodes must exist before creating a link");
        assertThat(graph.getLinks()).isEmpty();
    }

    @Test
    void addLinkWhenNodesExist() {
        var nodeA = new Node("A");
        var nodeB = new Node("B");
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        var linkToAdd = new Link(nodeA, nodeB);
        assertThatNoException().isThrownBy(() -> graph.addLink(linkToAdd));
        assertThat(graph.getLinks()).isEqualTo(Set.of(linkToAdd));
    }

    @Test
    void addLinkThatResultsInCycle() {
        var nodeA = new Node("A");
        var nodeB = new Node("B");
        var nodeC = new Node("C");
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        var linkAtoB = new Link(nodeA, nodeB);
        var linkBtoC = new Link(nodeB, nodeC);
        var linkCtoA = new Link(nodeC, nodeA);
        graph.addLink(linkAtoB);
        graph.addLink(linkBtoC);
        assertThatThrownBy(() -> graph.addLink(linkCtoA)).isInstanceOf(IllegalArgumentException.class).hasMessage("The link candidate will result in a cycle");
    }

    @Test
    void testGetChildNodesWhenNodesExist() {
        var nodeA = new Node("A");
        var nodeB = new Node("B");
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        var linkToAdd = new Link(nodeA, nodeB);
        graph.addLink(linkToAdd);
        assertThat(graph.getChildNodes(new Node("A"))).isEqualTo(Set.of(nodeB));
    }

    @Test
    void testGetChildNodesWhenMultipleNodesExist() {
        var nodeA = new Node("A");
        var nodeB = new Node("B");
        var nodeC = new Node("C");
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        var linkAtoB = new Link(nodeA, nodeB);
        var linkAtoC = new Link(nodeA, nodeC);
        graph.addLink(linkAtoB);
        graph.addLink(linkAtoC);
        assertThat(graph.getChildNodes(nodeA)).isEqualTo(Set.of(nodeB, nodeC));
    }

    @Test
    void testHasLink() {
        var nodeA = new Node("A");
        var nodeB = new Node("B");
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        var linkAtoB = new Link(nodeA, nodeB);
        graph.addLink(linkAtoB);
        assertThat(graph.hasLink(nodeA, nodeB)).isTrue();
    }

    @Test
    void testGetDescendantNodes() {
        var fluffy = new Node("Fluffy");
        var animal = new Node("Animal");
        var rabbit = new Node("Rabbit");
        var rat = new Node("Rat");
        var greyRat = new Node("Grey Rat");
        var blackRat = new Node("Black Rat");
        var rollandRat = new Node("Rolland Rat");

        graph.addNode(fluffy);
        graph.addNode(animal);
        graph.addNode(rabbit);
        graph.addNode(rat);
        graph.addNode(greyRat);
        graph.addNode(blackRat);
        graph.addNode(rollandRat);
        graph.addLink(new Link(fluffy, rabbit));
        graph.addLink(new Link(animal, rabbit));
        graph.addLink(new Link(animal, rat));
        graph.addLink(new Link(rat, greyRat));
        graph.addLink(new Link(rat, blackRat));
        graph.addLink(new Link(rat, rollandRat));

        assertThat(graph.getDescendantNodes(animal)).isEqualTo(Set.of(rabbit, rat, greyRat, blackRat, rollandRat));
    }

    @Test
    void testGetParentNodes() {
        var rat = new Node("Rat");
        var rollandRat = new Node("Rolland Rat");
        graph.addNode(rat);
        graph.addNode(rollandRat);
        graph.addLink(new Link(rat, rollandRat));

        assertThat(graph.getParentNodes(rollandRat)).isEqualTo(Set.of(rat));
    }

    @Test
    void testGetParentNodesWhenNoParentsExist() {
        var animal = new Node("Animal");
        graph.addNode(animal);

        assertThat(graph.getParentNodes(animal)).isEmpty();
    }

    @Test
    void testGetAncestorNodes() {
        var fluffy = new Node("Fluffy");
        var animal = new Node("Animal");
        var rabbit = new Node("Rabbit");
        var rat = new Node("Rat");
        var greyRat = new Node("Grey Rat");
        var blackRat = new Node("Black Rat");
        var rollandRat = new Node("Rolland Rat");

        graph.addNode(fluffy);
        graph.addNode(animal);
        graph.addNode(rabbit);
        graph.addNode(rat);
        graph.addNode(greyRat);
        graph.addNode(blackRat);
        graph.addNode(rollandRat);
        graph.addLink(new Link(fluffy, rabbit));
        graph.addLink(new Link(animal, rabbit));
        graph.addLink(new Link(animal, rat));
        graph.addLink(new Link(rat, greyRat));
        graph.addLink(new Link(rat, blackRat));
        graph.addLink(new Link(rat, rollandRat));

        assertThat(graph.getAncestorsNode(rollandRat)).isEqualTo(Set.of(animal, rat));
    }
}
