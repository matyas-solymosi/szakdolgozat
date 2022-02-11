package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.storage.GridStorage;
import lombok.AllArgsConstructor;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class DiamondSquareService {

    private static int minimumValue = 0;

    /**
     * Generates noise on the given grid using the Diamond-square algorithm.

     * @param gridId The ID of the grid.
     * @param randRange The range of the randomly generated numbers.
     * @param startingValue The initial values of the algorithm.
     * @return A grid with the generated noise.
     * @throws NotFoundException
     */
    public static Grid generateNoise(String gridId, int randRange, int startingValue) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }

        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        minimumValue = 0;
        int size = grid.getSize() - 1;

        grid.getNodes()[0][0].setHeight(startingValue);
        grid.getNodes()[size][size].setHeight(startingValue);
        grid.getNodes()[size][0].setHeight(startingValue);
        grid.getNodes()[0][size].setHeight(startingValue);

        divide(grid.getGridId(), size, randRange);

        if (minimumValue < 0) {
            for (int row = 0; row <= size; row++) {
                for (int col = 0; col <= size; col++) {
                    grid.getNodes()[row][col].setHeight(grid.getNodes()[row][col].getHeight() + Math.abs(minimumValue));
                }
            }
        }
        return grid;
    }

    /**
     * Performs a step of the Diamond-Square algorithm on a subgrid of the given grid.
     *
     * @param gridId The ID of the grid.
     * @param size The size of the current subgrid.
     * @param randRange The range of the randomly generated numbers.
     * @throws NotFoundException
     */
    public static void divide(String gridId, int size, int randRange) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        int currentSize = size / 2;
        if (size / 2 < 1)
            return;
        for (int col = currentSize; col < grid.getSize() - 1; col += size) {
            for (int row = currentSize; row < grid.getSize() - 1; row += size) {
                square(grid.getGridId(), row, col, currentSize, randRange);
            }
        }
        for (int col = 0; col <= grid.getSize() - 1; col += currentSize) {
            for (int row = (col + currentSize) % size; row <= grid.getSize() - 1; row += size) {
                diamond(grid.getGridId(), row, col, currentSize, randRange);
            }
        }
        divide(grid.getGridId(), currentSize, randRange / 2);
    }

    /**
     * Performs the square step on the given cell of the given grid.
     *
     * @param gridId The ID of the grid.
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @param size The size of the current subgrid.
     * @param randRange The range of the randomly generated numbers.
     * @throws NotFoundException
     */
    public static synchronized void square(String gridId, int row, int col, int size, int randRange) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        int sum = 0;
        int cnt = 0;
        if (grid.isInBounds(row - size, col - size)) {
            cnt++;
            sum += grid.getNodes()[row - size][col - size].getHeight();
        }
        if (grid.isInBounds(row + size, col + size)) {
            cnt++;
            sum += grid.getNodes()[row + size][col + size].getHeight();
        }
        if (grid.isInBounds(row + size, col - size)) {
            cnt++;
            sum += grid.getNodes()[row + size][col - size].getHeight();
        }
        if (grid.isInBounds(row - size, col + size)) {
            cnt++;
            sum += grid.getNodes()[row - size][col + size].getHeight();
        }
        int weight = sum / cnt + (int) Math.round(Math.random() * randRange * 2 - randRange);
        grid.getNodes()[row][col].setHeight(weight);
        if (weight < minimumValue) {
            minimumValue = weight;
        }
    }

    /**
     * Performs the square step on the given cell of the given grid.
     *
     * @param gridId The ID of the grid.
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @param size The size of the current subgrid.
     * @param randRange The range of the randomly generated numbers.
     * @throws NotFoundException
     */
    public static void diamond(String gridId, int row, int col, int size, int randRange) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        int sum = 0;
        int cnt = 0;
        if (grid.isInBounds(row, col - size)) {
            cnt++;
            sum += grid.getNodes()[row][col - size].getHeight();
        }
        if (grid.isInBounds(row + size, col)) {
            cnt++;
            sum += grid.getNodes()[row + size][col].getHeight();
        }
        if (grid.isInBounds(row, col + size)) {
            cnt++;
            sum += grid.getNodes()[row][col + size].getHeight();
        }
        if (grid.isInBounds(row - size, col)) {
            cnt++;
            sum += grid.getNodes()[row - size][col].getHeight();
        }
        int weight = sum / cnt + (int) Math.round(Math.random() * randRange * 2 - randRange);
        grid.getNodes()[row][col].setHeight(weight);
        if (weight < minimumValue) {
            minimumValue = weight;
        }
    }
}
