package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.exception.NotValidSizeException;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.model.GridClick;
import com.cshrob.szakdolgozat.model.Node;
import com.cshrob.szakdolgozat.storage.GridStorage;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class GridService {
    /**
     * Generates a new grid with the given size.
     *
     * @param size The size of the grid.
     * @return The generated grid.
     */
    public static Grid generateGrid(int size) {
        Grid grid = new Grid(size);
        GridStorage.getInstance().setGrid(grid);
        return grid;
    }

    /**
     * Resets the values of the given grid.
     *
     * @param gridId The ID of the grid.
     * @return The grid.
     * @throws NotFoundException
     */
    public static Grid resetGrid(String gridId) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        grid.setStartNode(null);
        grid.setFinishNode(null);
        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize(); col++) {
                Node node = grid.getNodes()[row][col];
                node.setBlocked(false);
                node.setHeight(0);
                node.setParent(null);
                node.setFScore(Double.POSITIVE_INFINITY);
                node.setGScore(Double.POSITIVE_INFINITY);
            }
        }
        return grid;
    }

    /**
     * Performs a Click with the given type on the given cell of the given grid.
     *
     * @param gridClick Contains the type of the click, the coordinates of the cell and the ID of the grid.
     * @return The grid with the performed modification.
     * @throws NotFoundException
     */
    public static Grid gridClick(GridClick gridClick) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridClick.getGridId())) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridClick.getGridId());
        Node[][] nodes = grid.getNodes();
        switch (gridClick.getClickType()) {
            case INCREASE:
                grid.getNodes()[gridClick.getRow()][gridClick.getColumn()]
                        .setHeight(grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].getHeight() + 1);
                break;
            case DECREASE:
                if (grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].getHeight() > 0) {
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()]
                            .setHeight(grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].getHeight() - 1);
                }
                break;
            case SET_START:
                if (grid.getFinishNode() != null && gridClick.getRow() == grid.getFinishNode().getRow() && gridClick.getColumn() == grid.getFinishNode().getColumn()) {
                    grid.setFinishNode(null);
                } else if (grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].isBlocked()) {
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].setBlocked(false);
                }
                grid.setStartNode(nodes[gridClick.getRow()][gridClick.getColumn()]);
                break;
            case SET_FINISH:
                if (grid.getStartNode() != null && gridClick.getRow() == grid.getStartNode().getRow() && gridClick.getColumn() == grid.getStartNode().getColumn()) {
                    grid.setStartNode(null);
                } else if (grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].isBlocked()) {
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].setBlocked(false);
                }
                grid.setFinishNode(nodes[gridClick.getRow()][gridClick.getColumn()]);
                break;
            case BLOCK:
                if (grid.getStartNode() != null && gridClick.getRow() == grid.getStartNode().getRow() && gridClick.getColumn() == grid.getStartNode().getColumn()) {
                    grid.setStartNode(null);
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].setBlocked(true);
                } else if (grid.getFinishNode() != null && gridClick.getRow() == grid.getFinishNode().getRow() && gridClick.getColumn() == grid.getFinishNode().getColumn()) {
                    grid.setFinishNode(null);
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].setBlocked(true);
                } else
                    grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].setBlocked(!grid.getNodes()[gridClick.getRow()][gridClick.getColumn()].isBlocked());
                break;
        }
        return grid;

    }

    /**
     * Returns the vertical and horizontal neighbours of the given cell on the given grid.
     *
     * @param gridId The id of the grid.
     * @param node   The node.
     * @return An ArrayList containing the neighbours.
     * @throws NotFoundException
     */
    public static ArrayList<Node> getNeighbours(String gridId, Node node) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        ArrayList<Node> neighbours = new ArrayList<>();

        if (node.getRow() < grid.getSize() - 1 && !grid.getNodes()[node.getRow() + 1][node.getColumn()].isBlocked())
            neighbours.add(grid.getNodes()[node.getRow() + 1][node.getColumn()]);
        if (node.getRow() > 0 && !grid.getNodes()[node.getRow() - 1][node.getColumn()].isBlocked())
            neighbours.add(grid.getNodes()[node.getRow() - 1][node.getColumn()]);
        if (node.getColumn() < grid.getSize() - 1 && !grid.getNodes()[node.getRow()][node.getColumn() + 1].isBlocked())
            neighbours.add(grid.getNodes()[node.getRow()][node.getColumn() + 1]);
        if (node.getColumn() > 0 && !grid.getNodes()[node.getRow()][node.getColumn() - 1].isBlocked())
            neighbours.add(grid.getNodes()[node.getRow()][node.getColumn() - 1]);

        return neighbours;
    }

    /**
     * Checks if the given node is in bounds of the given grid.
     *
     * @param gridId The id of the grid.
     * @param node   The examined node.
     * @return True if it is bounds, false if it is not.
     * @throws NotFoundException
     */
    public static boolean isValid(String gridId, Node node) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        if (grid.getSize() > 0 && grid.getSize() > 0)
            return (node.getRow() >= 0) && (node.getRow() < grid.getSize())
                    && (node.getColumn() >= 0) && (node.getRow() < grid.getSize());
        return false;
    }

    /**
     * Reads the height values from the given file and loads them into the given grid.
     *
     * @param gridId The ID of the grid.
     * @param fileName The name of the file.
     * @return The grid with the new height values.
     * @throws NotFoundException
     * @throws IOException
     * @throws NotValidSizeException
     */
    public static Grid loadGrid(String gridId, String fileName) throws NotFoundException, IOException, NotValidSizeException {

        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        int lines = 0;
        String line = reader.readLine();
        while (line != null){
            int rows = 0;
            for (String s: line.split(" ")) {
                rows++;
            }
            if(rows != grid.getSize()) {
                throw new NotValidSizeException("File does not contain a valid sized map.");
            }
            lines++;
            line = reader.readLine();
        }
        reader.close();
        if(lines != grid.getSize()) {
            throw new NotValidSizeException("File does not contain a valid sized map.");
        }

        reader = new BufferedReader(new FileReader(fileName));
        for (int i = 0; i < 33; i++) {
            String[] values = reader.readLine().split(" ");
            for (int j = 0; j < 33; j++) {
                grid.getNodes()[i][j].setHeight(Integer.parseInt(values[j]));
            }
        }
        return grid;
    }

    /**
     * Creates a savable string from the height values of the given grid.
     *
     * @param gridId The ID of the grid.
     * @return The savable string.
     * @throws NotFoundException
     */
    public static String saveGrid(String gridId) throws NotFoundException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }
        Grid grid = GridStorage.getInstance().getGrids().get(gridId);
        String saveFile = "";

        for (int i = 0; i < 33; i++) {
            String line = "";
            for (int j = 0; j < 33; j++) {
                line += grid.getNodes()[i][j].getHeight() + " ";
            }
            saveFile += line + '\n';
        }

        return saveFile;
    }

}
