package com.cshrob.szakdolgozat.storage;

import com.cshrob.szakdolgozat.model.AStar;
import com.cshrob.szakdolgozat.model.Grid;

import java.util.HashMap;
import java.util.Map;

public class AStarStorage {

    private static Map<String, AStar> aStars;
    private static AStarStorage instance;

    private AStarStorage() {
        aStars = new HashMap<>();
    }
    public static synchronized AStarStorage getInstance() {
        if(instance == null) {
            instance = new AStarStorage();
        }
        return instance;
    }
    public Map<String, AStar> getAStars() {
        return aStars;
    }
    public void setAStar(AStar aStar) {
        aStars.put(aStar.getAStarId(), aStar);
    }

}
