package com.cshrob.szakdolgozat.service;

import com.cshrob.szakdolgozat.exception.NotConfiguredException;
import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.model.AStar;
import com.cshrob.szakdolgozat.model.Grid;
import com.cshrob.szakdolgozat.model.Node;
import com.cshrob.szakdolgozat.storage.AStarStorage;
import com.cshrob.szakdolgozat.storage.GridStorage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static java.lang.Math.abs;

@Service
public class AStarService {

    /**
     * Initializes an AStar algorithm instance on the given grid.
     *
     * @param gridId The ID of the grid.
     * @return The initialized AStar instance.
     * @throws NotFoundException
     * @throws NotConfiguredException
     */
    public static AStar initializeAlgorithm(String gridId) throws NotFoundException, NotConfiguredException {
        if (!GridStorage.getInstance().getGrids().containsKey(gridId)) {
            throw new NotFoundException("Grid instance not found.");
        }

        Grid grid = GridStorage.getInstance().getGrids().get(gridId);

        if (grid.getStartNode() == null && grid.getFinishNode() == null) {
            throw new NotConfiguredException("Start and finish node not chosen.");
        }
        if (grid.getStartNode() == null) {
            throw new NotConfiguredException("Start node not chosen.");
        }
        if (grid.getFinishNode() == null) {
            throw new NotConfiguredException("Finish node not chosen.");
        }

        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize(); col++) {
                Node node = grid.getNodes()[row][col];
                node.setFScore(Double.POSITIVE_INFINITY);
                node.setGScore(Double.POSITIVE_INFINITY);
            }
        }
        AStar aStar = new AStar(grid);
        Node startNode = grid.getStartNode();

        startNode.setGScore(0.0);
        startNode.setFScore(h(startNode, grid.getFinishNode()));
        startNode.setParent(startNode);

        AStarStorage.getInstance().setAStar(aStar);
        aStar.getOpenNodes().add(startNode);

        return aStar;
    }

    /**
     * Performs a step on the given AStar algorithm.
     *
     * @param aStarId The ID of the algorithm.
     * @return The algorithm with the updated values.
     * @throws NotFoundException
     */
    public static AStar stepAlgorithm(String aStarId) throws NotFoundException {
        if (!AStarStorage.getInstance().getAStars().containsKey(aStarId)) {
            throw new NotFoundException("AStar instance not found.");
        }
        AStar aStar = AStarStorage.getInstance().getAStars().get(aStarId);

        if (aStar.getOpenNodes().isEmpty()) {
            aStar.setFinished(true);
            return aStar;
        }
        aStar.setCurrent(aStar.getOpenNodes().poll());
        aStar.getClosedNodes().add(aStar.getCurrent());
        for (Node node : GridService.getNeighbours(aStar.getGrid().getGridId(), aStar.getCurrent())) {
            if (aStar.getGrid().isInBounds(node.getRow(), node.getColumn())) {
                if ((node.getRow() == aStar.getGrid().getFinishNode().getRow()) && (node.getColumn() == aStar.getGrid().getFinishNode().getColumn())) {
                    node.setParent(aStar.getCurrent());
                    aStar = tracePath(aStar.getAStarId(), node);
                    aStar.setFinished(true);
                    return aStar;
                }
                if (aStar.getClosedNodes().contains(node)) {
                    continue;
                }
                double gTemp = aStar.getCurrent().getGScore() + 1 + abs(node.getHeight() - aStar.getCurrent().getHeight());
                if (gTemp < node.getGScore()) {
                    node.setParent(aStar.getCurrent());
                    node.setGScore(gTemp);
                    node.setFScore(gTemp + h(node, aStar.getGrid().getFinishNode()));
                    aStar.getOpenNodes().add(node);
                    aStar = tracePath(aStar.getAStarId(), node);
                }
            }
        }
        return aStar;
    }

    /**
     * Traces back from the given node to the starting node of the given AStar algorithm.
     *
     * @param aStarId The ID of the AStar algorithm.
     * @param node The node from which the tracing starts.
     * @return
     */
    public static AStar tracePath(String aStarId, Node node) throws NotFoundException {
        if (!AStarStorage.getInstance().getAStars().containsKey(aStarId)) {
            throw new NotFoundException("AStar instance not found.");
        }

        AStar aStar = AStarStorage.getInstance().getAStars().get(aStarId);
        Grid grid = aStar.getGrid();
        Node current = grid.getNodes()[node.getRow()][node.getColumn()];
        aStar.setShortestPath(new ArrayList<>());
        aStar.setShortestPathLength(0);

        if (current == grid.getFinishNode()) {
            aStar.setShortestPathLength(
                    (int) (aStar.getGrid().getFinishNode().getParent().getGScore() + 1
                            + abs(aStar.getGrid().getFinishNode().getHeight() - aStar.getGrid().getFinishNode().getParent().getHeight())));
        }
        do {
            aStar.getShortestPath().add(current);
            current = current.getParent();

        } while (!(current.getRow() == grid.getStartNode().getRow() && current.getColumn() == grid.getStartNode().getColumn()));

        aStar.getShortestPath().add(aStar.getGrid().getStartNode());

        return aStar;
    }

    /**
     * The heuristic function used by the A* algorithm, calculates the manhattan distance of the given nodes
     * @param node The examined node.
     * @param finish The finish node of the algorithm.
     * @return The heuristic value.
     */
    public static float h(Node node, Node finish) {
        //manhattan distance
        return abs(node.getRow() - finish.getRow()) + abs(node.getColumn() - finish.getColumn());
    }

}

