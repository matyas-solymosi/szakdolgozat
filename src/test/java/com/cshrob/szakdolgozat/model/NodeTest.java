package com.cshrob.szakdolgozat.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {
    /**
     * Tests the comparison of two cells with equal F score
     */
    @Test
    void compareToSmaller() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(0, 0);
        node1.setFScore(0);
        node2.setFScore(0);

        assertEquals(0, node2.compareTo(node1));
    }

    /**
     * Tests the comparison of two cells with different F score
     */
    @Test
    void compareToEqual() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(0, 0);
        node1.setFScore(0);
        node2.setFScore(1);

        assertEquals(1, node2.compareTo(node1));
    }

    /**
     * Tests the comparison of two cells with different F score
     */
    @Test
    void compareToHigher() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(0, 0);
        node1.setFScore(0);
        node2.setFScore(-1);

        assertEquals(-1, node2.compareTo(node1));
    }
}