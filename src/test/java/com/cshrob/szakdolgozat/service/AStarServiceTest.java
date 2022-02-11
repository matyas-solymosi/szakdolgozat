package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotConfiguredException;
import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.model.AStar;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.model.Node;
import com.cshrob.szakdolgozat.storage.AStarStorage;
import com.cshrob.szakdolgozat.storage.GridStorage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AStarServiceTest {
    /**
     * Tests if the initialization of an AStar instance works properly.
     *
     * @throws NotFoundException
     * @throws NotConfiguredException
     */
    @Test
    void initializeAlgorithm() throws NotFoundException, NotConfiguredException {
        Grid grid = new Grid(33);
        GridStorage.getInstance().setGrid(grid);

        grid.setStartNode(new Node(0, 0));
        grid.setFinishNode(new Node(0, 5));
        AStar aStar = AStarService.initializeAlgorithm(grid.getGridId());

        assertEquals(0.0, grid.getStartNode().getGScore());
        assertEquals(5, grid.getStartNode().getFScore());
        assertEquals(grid.getStartNode(), grid.getStartNode().getParent());

        assertTrue(aStar.getOpenNodes().contains(grid.getStartNode()));
    }

    /**
     * Initializes an AStar instance, then tests the stepping on the algorithm.
     *
     * @throws NotFoundException
     * @throws NotConfiguredException
     */
    @Test
    void stepAlgorithm() throws NotFoundException, NotConfiguredException {
        Grid grid = new Grid(33);
        GridStorage.getInstance().setGrid(grid);

        grid.setStartNode(new Node(0, 0));
        grid.setFinishNode(new Node(0, 5));
        AStar aStar = AStarService.initializeAlgorithm(grid.getGridId());

        AStarService.stepAlgorithm(aStar.getAStarId());

        assertFalse(aStar.getOpenNodes().contains(grid.getStartNode()));
        assertTrue(aStar.getOpenNodes().contains(grid.getNodes()[0][1]));
        assertTrue(aStar.getOpenNodes().contains(grid.getNodes()[1][0]));
        assertEquals(grid.getStartNode(), grid.getNodes()[1][0].getParent());
        assertEquals(1, grid.getNodes()[0][1].getGScore());
        assertEquals(5, grid.getNodes()[0][1].getFScore());

    }

    /**
     * Initializes an AStar instance, creates a parent-child chain from the start node to a node, then tests the tracePath() method.
     *
     * @throws NotFoundException
     * @throws NotConfiguredException
     */
    @Test
    void tracePath() throws NotFoundException, NotConfiguredException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        grid.setStartNode(grid.getNodes()[0][0]);
        grid.setFinishNode(grid.getNodes()[4][0]);
        grid.getNodes()[1][0].setParent(grid.getNodes()[0][0]);
        grid.getNodes()[2][0].setParent(grid.getNodes()[1][0]);
        grid.getNodes()[3][0].setParent(grid.getNodes()[2][0]);

        AStar aStar = AStarService.initializeAlgorithm(grid.getGridId());
        AStarStorage.getInstance().setAStar(aStar);

        aStar = AStarService.tracePath(aStar.getAStarId(), aStar.getGrid().getNodes()[3][0]);

        ArrayList<Node> list = new ArrayList<>();
        list.add(grid.getNodes()[3][0]);
        list.add(grid.getNodes()[2][0]);
        list.add(grid.getNodes()[1][0]);
        list.add(grid.getNodes()[0][0]);
        assertEquals(list, aStar.getShortestPath());

    }

    /**
     * Tests if the heuristic function works properly.
     */
    @Test
    void h() {
        Node node1 = new Node(0, 0);
        Node node2 = new Node(0, 5);
        assertEquals(5, AStarService.h(node1, node2));
    }
}