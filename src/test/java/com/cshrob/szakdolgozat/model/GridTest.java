package com.cshrob.szakdolgozat.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GridTest {

    /**
     * Test the isInBounds method with a valid node.
     */
    @Test
    void isInBoundsTrue() {
        Grid grid = new Grid(5);
        assertTrue(grid.isInBounds(0, 0));
    }

    /**
     * Test the isInBounds method with an invalid node.
     */
    @Test
    void isInBoundsFalse() {
        Grid grid = new Grid(5);
        assertFalse(grid.isInBounds(0, 5));
    }
}