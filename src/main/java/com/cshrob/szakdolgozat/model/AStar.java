package com.cshrob.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.UUID;

@Data
public class AStar {
    private String aStarId;
    @JsonManagedReference
    private Grid grid;
    private PriorityQueue<Node> openNodes;
    private ArrayList<Node> shortestPath;
    private ArrayList<Node> closedNodes;
    private Node current;
    private int shortestPathLength;
    private boolean isFinished;

    public AStar(Grid grid) {
        this.aStarId = UUID.randomUUID().toString();
        this.grid = grid;
        this.isFinished = false;
        this.shortestPath = new ArrayList<>();
        this.openNodes = new PriorityQueue<>();
        this.closedNodes = new ArrayList<>();
    }


}

