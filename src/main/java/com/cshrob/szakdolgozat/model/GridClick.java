package com.cshrob.szakdolgozat.model;

import lombok.Data;

@Data
public class GridClick {
    private ClickType clickType;
    private int row;
    private int column;
    private String gridId;

    public GridClick(ClickType clickType, int row, int column, String gridId) {
        this.clickType = clickType;
        this.row = row;
        this.column = column;
        this.gridId = gridId;
    }
}
