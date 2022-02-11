package com.cshrob.szakdolgozat.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

@Data
public class Node implements Comparable<Node> {

    private int row;
    private int column;
    private int height;

    private boolean blocked = false;
    @ToString.Exclude
    @JsonBackReference
    private Node parent;
    private double fScore;
    private double gScore;

    public Node(int row, int column) {
        this.row = row;
        this.column = column;
        this.parent = null;
        this.fScore = Double.POSITIVE_INFINITY;
        this.gScore = Double.POSITIVE_INFINITY;
        this.height = 0;
    }

    @Override
    public int compareTo(Node node) {
        if (this.getFScore() < node.getFScore()) {
            return -1;
        } else if (this.getFScore() == node.getFScore()) {
            if (this.getFScore() - this.getGScore() < node.getFScore() - node.getGScore()) {
                return -1;
            }
            return 0;
        } else {
            return 1;
        }
    }
}
