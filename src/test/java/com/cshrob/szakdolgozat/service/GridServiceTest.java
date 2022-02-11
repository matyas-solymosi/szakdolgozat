package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.model.ClickType;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.model.GridClick;
import com.cshrob.szakdolgozat.model.Node;
import com.cshrob.szakdolgozat.storage.GridStorage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GridServiceTest {
    /**
     * Tests the generation of a grid instance.
     */
    @Test
    void generateGrid() {
        Grid grid = GridService.generateGrid(5);
        assertEquals(5, grid.getSize());
    }

    /**
     * Tests the resetting of a grid instance.
     *
     * @throws NotFoundException
     */
    @Test
    void resetGrid() throws NotFoundException {
        Grid grid = GridService.generateGrid(5);

        grid.setStartNode(grid.getNodes()[0][0]);
        grid.getNodes()[0][1].setBlocked(true);
        grid.getNodes()[0][1].setHeight(5);
        grid.getNodes()[0][1].setParent(grid.getNodes()[0][0]);
        grid.getNodes()[0][1].setFScore(5);
        grid.getNodes()[0][1].setGScore(5);

        GridService.resetGrid(grid.getGridId());

        assertNull(grid.getStartNode());
        assertFalse(grid.getNodes()[0][1].isBlocked());
        assertEquals(0, grid.getNodes()[0][1].getHeight());
        assertNull(grid.getNodes()[0][1].getParent());
        assertEquals(Double.POSITIVE_INFINITY, grid.getNodes()[0][1].getFScore());
        assertEquals(Double.POSITIVE_INFINITY, grid.getNodes()[0][1].getGScore());

    }

    /**
     * Tests the clicking of a node, with the Increase modifier.
     *
     * @throws Exception
     */
    @Test
    void gridClickIncrease() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        GridService.gridClick(new GridClick(ClickType.INCREASE, 0, 0, grid.getGridId()));

        assertEquals(1, grid.getNodes()[0][0].getHeight());
    }

    /**
     * Tests the clicking of a node, with the Decrease modifier.
     *
     * @throws Exception
     */
    @Test
    void gridClickDecrease() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);
        grid.getNodes()[0][0].setHeight(5);
        GridService.gridClick(new GridClick(ClickType.DECREASE, 0, 0, grid.getGridId()));

        assertEquals(4, grid.getNodes()[0][0].getHeight());
    }

    /**
     * Tests the clicking of a node, with the Set_start modifier.
     *
     * @throws Exception
     */
    @Test
    void gridClickSetStart() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        GridService.gridClick(new GridClick(ClickType.SET_START, 0, 0, grid.getGridId()));

        assertEquals(grid.getNodes()[0][0], grid.getStartNode());
    }

    /**
     * Tests the clicking of a node, with the Set_finish modifier.
     *
     * @throws Exception
     */
    @Test
    void gridClickSetFinish() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        GridService.gridClick(new GridClick(ClickType.SET_FINISH, 0, 0, grid.getGridId()));

        assertEquals(grid.getNodes()[0][0], grid.getFinishNode());
    }

    /**
     * Tests the clicking of a node, with the Block modifier.
     *
     * @throws Exception
     */
    @Test
    void gridClickSetBlock() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        GridService.gridClick(new GridClick(ClickType.BLOCK, 0, 0, grid.getGridId()));

        assertTrue(grid.getNodes()[0][0].isBlocked());
    }

    /**
     * Tests the finding of the neighbours of a cell.
     *
     * @throws NotFoundException
     */
    @Test
    void getNeighbours() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        ArrayList<Node> list = new ArrayList<Node>();
        list.add(grid.getNodes()[2][1]);
        list.add(grid.getNodes()[0][1]);
        list.add(grid.getNodes()[1][2]);
        list.add(grid.getNodes()[1][0]);

        assertEquals(list, GridService.getNeighbours(grid.getGridId(), grid.getNodes()[1][1]));

    }

    /**
     * Tests the isValid() function, with a valid node.
     *
     * @throws NotFoundException
     */
    @Test
    void isValidTrue() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        assertTrue(GridService.isValid(grid.getGridId(), new Node(0, 0)));
    }

    /**
     * Tests the isValid() function, with an invalid node.
     *
     * @throws NotFoundException
     */
    @Test
    void isValidFalse() throws NotFoundException {
        Grid grid = new Grid(5);
        GridStorage.getInstance().setGrid(grid);

        assertFalse(GridService.isValid(grid.getGridId(), new Node(5, 5)));

    }
}