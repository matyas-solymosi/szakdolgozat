package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.storage.GridStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DiamondSquareServiceTest {

    /**
     * Tests whether the noise generation algorithm generates any negative numbers.
     *
     * @throws NotFoundException
     */
    @Test
    void generateNoise() throws NotFoundException {
        Grid grid = new Grid(33);
        GridStorage.getInstance().setGrid(grid);

        grid = DiamondSquareService.generateNoise(grid.getGridId(), 10, 10);

        for (int row = 0; row <= grid.getSize() - 1; row++) {
            for (int col = 0; col <= grid.getSize() - 1; col++) {
                assertTrue(grid.getNodes()[row][col].getHeight() >= 0);
            }
        }
    }
}