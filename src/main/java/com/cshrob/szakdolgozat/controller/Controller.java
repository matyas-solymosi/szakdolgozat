package com.cshrob.szakdolgozat.controller;

import com.cshrob.szakdolgozat.exception.NotConfiguredException;
import com.cshrob.szakdolgozat.exception.NotFoundException;
import com.cshrob.szakdolgozat.exception.NotValidSizeException;
import com.cshrob.szakdolgozat.model.*;
import com.cshrob.szakdolgozat.service.AStarService;
import com.cshrob.szakdolgozat.service.DiamondSquareService;
import com.cshrob.szakdolgozat.service.GridService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("app")
public class Controller {

    private GridService gridService;
    private AStarService aStarService;
    private DiamondSquareService diamondSquareService;

    @PostMapping(path = "/generateGrid")
    public ResponseEntity<Grid> generateGrid(@RequestBody Integer size) {
        log.info("Generating grid with size: {}", size);
        return ResponseEntity.ok(gridService.generateGrid(size));
    }

    @PostMapping(path = "/resetGrid")
    public ResponseEntity<Grid> resetGrid(@RequestBody String gridId) throws NotFoundException {
        log.info("Resetting grid: {}", gridId);
        return ResponseEntity.ok(gridService.resetGrid(gridId));
    }

    @PostMapping(path = "/loadGrid")
    public ResponseEntity<Grid> loadGrid(@RequestBody LoadData data) throws NotFoundException, IOException, NotValidSizeException {
        log.info("Loading grid from the file: {}", data.getFileName());
        return ResponseEntity.ok(gridService.loadGrid(data.getGridId(), data.getFileName()));
    }

    @PostMapping(path = "/saveGrid")
    public ResponseEntity<String> saveGrid(@RequestBody String gridId) throws NotFoundException, IOException {
        log.info("Saving grid: {}", gridId);
        return ResponseEntity.ok(gridService.saveGrid(gridId));
    }

    @PostMapping(path = "/generateNoise")
    public ResponseEntity<Grid> generateNoise(@RequestBody NoiseData data) throws NotFoundException {
        log.info("Generating noise on grid: {}", data.getGridId());
        return ResponseEntity.ok(diamondSquareService.generateNoise(data.getGridId(), data.getRandRange(), data.getStartingValue()));
    }

    @PostMapping(path = "/clickNode")
    public ResponseEntity<Grid> clickNode(@RequestBody GridClick data) throws Exception {
        log.info("{} click on node({},{})", data.getClickType(), data.getColumn(), data.getRow());
        return ResponseEntity.ok(gridService.gridClick(data));
    }

    @PostMapping(path = "/initializeAlgorithm")
    public ResponseEntity<AStar> initializeAlgorithm(@RequestBody String gridId) throws NotFoundException, NotConfiguredException {
        log.info("Running AStar on grid: {}", gridId);
        return ResponseEntity.ok(aStarService.initializeAlgorithm(gridId));
    }

    @PostMapping(path = "/stepAlgorithm")
    public ResponseEntity<AStar> stepAlgorithm(@RequestBody String aStarId) throws NotFoundException {
        log.info("Step on aStar : {}", aStarId);
        return ResponseEntity.ok(aStarService.stepAlgorithm(aStarId));
    }

}
