package com.cshrob.szakdolgozat.storage;

import com.cshrob.szakdolgozat.model.Grid;

import java.util.HashMap;
import java.util.Map;

public class GridStorage {

    private static Map<String, Grid> grids;
    private static GridStorage instance;

    private GridStorage() {
        grids = new HashMap<>();
    }
    public static synchronized GridStorage getInstance() {
        if(instance == null) {
            instance = new GridStorage();
        }
        return instance;
    }
    public Map<String, Grid> getGrids() {
        return grids;
    }
    public void setGrid(Grid grid) {
        grids.put(grid.getGridId(), grid);
    }

}
