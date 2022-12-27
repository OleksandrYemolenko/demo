package com.example.demo.controllers;

import com.example.demo.entities.database.Database;
import com.example.demo.entities.database.Table;
import com.example.demo.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(path ="/api/v1/database")
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @PostMapping
    public void createDatabase(@RequestBody Database database) throws SQLException {
        databaseService.createDatabase(database);
    }

    @PutMapping
    public void manipulateDatabase(@RequestBody Table table, @RequestParam String method) throws SQLException, IOException {
        if(method.equals("save")) {
            databaseService.saveTable(table);
        } else {
            databaseService.loadTable(table);
        }
    }
}
