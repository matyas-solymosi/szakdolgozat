package com.cshrob.szakdolgozat.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Grid {
    private int size;
    private String gridId;
    private Node[][] nodes;
    private Node startNode;
    private Node finishNode;

    public Grid(int size) {
        Node[][] nodes = new Node[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                nodes[row][col]=new Node(row,col);
            }
        }
        this.nodes = nodes;
        this.size = size;
        this.startNode = null;
        this.finishNode = null;
        this.gridId = UUID.randomUUID().toString();
    }
    public boolean isInBounds(int row, int col){
        return row < this.getSize() && row > -1 && col < this.getSize() && col > -1;
    }



}
