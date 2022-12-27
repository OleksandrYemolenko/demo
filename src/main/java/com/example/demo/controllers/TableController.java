package com.example.demo.controllers;

import com.example.demo.entities.database.Table;
import com.example.demo.services.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(path ="/api/v1/table")
public class TableController {

    Logger logger = LoggerFactory.getLogger(TableController.class);

    @Autowired
    DatabaseService databaseService;

    @GetMapping
    public ResponseEntity<Table> viewTable(@RequestBody Table table) throws SQLException {
        Table resultTable = databaseService.viewTable(table);

        return ResponseEntity.ok(resultTable);
    }

    @PostMapping
    public void createTable(@RequestBody Table table) throws Exception {
        databaseService.createTable(table);
    }

    @PatchMapping
    public void updateTable(@RequestBody Table table, @RequestParam String filterName, @RequestParam String filterValue) throws SQLException {
        databaseService.updateTable(table, filterName, filterValue);
    }

    @DeleteMapping
    public void deleteTable(@RequestBody Table table) throws SQLException {
        databaseService.deleteTable(table);
    }
}
