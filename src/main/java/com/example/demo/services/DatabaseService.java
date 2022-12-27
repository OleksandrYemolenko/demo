package com.example.demo.services;

import com.example.demo.entities.database.Column;
import com.example.demo.entities.database.Database;
import com.example.demo.entities.database.Table;
import com.example.demo.utils.DataTypes;
import org.postgresql.copy.CopyManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.postgresql.PGConnection;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DatabaseService {

    Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    static final String BASE_URL = "jdbc:postgresql://localhost:5432/";
    static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";

    static final String USER = "root";
    static final String PASSWORD = "root";

    public void createDatabase(Database database) throws SQLException{
        Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
        Statement statement = connection.createStatement();

        String query = "CREATE DATABASE " + database.getName();

        logger.info("Creating database... {}", database);

        int result = statement.executeUpdate(query);

        logger.info("Successfully created database: {}", database);

        statement.close();
        connection.close();
    }

    @Modifying
    public void createTable(Table table) throws Exception {
        Connection connection = DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        Statement statement = connection.createStatement();

        validate(table);

        String query = createTableQuery(table);

        logger.info("Creating table in database...");
        statement.executeUpdate(query);

        logger.info("Successfully created table: {}", table.getName());

        statement.close();
        connection.close();
    } //TODO: validation

    public void updateTable(Table table, String filterName, String filterValue) throws SQLException {
        Connection connection = DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        Statement statement = connection.createStatement();

        String query = updateTableQuery(table, filterName, filterValue);

        logger.info("Updating table in database...");
        statement.executeUpdate(query);

        logger.info("Successfully Updating table: {}", table.getName());

        statement.close();
        connection.close();
    }

    public Table viewTable(Table table) throws SQLException{
        Connection connection = DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        Statement statement = connection.createStatement();

        String query = "SELECT * FROM " + table.getName();

        logger.info("Retrieving data from database...");
        ResultSet resultSet = statement.executeQuery(query);

        Table resultTable =  new Table(table.getName(), table.getDatabase(), table.getColumns(), printTable(table, resultSet));

        resultSet.close();
        statement.close();
        connection.close();

        return resultTable;
    }

    public void deleteTable(Table table) throws SQLException{
        Connection connection = DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        Statement statement = connection.createStatement();

        String query = "DROP TABLE " + table.getName();

        logger.info("Deleting table from database...");

        statement.executeUpdate(query);

        logger.info("Successfully deleted table: {}", table.getName());

        statement.close();
        connection.close();
    }

    public void saveTable(Table table) throws SQLException, IOException {
        PGConnection connection = (PGConnection) DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        CopyManager copyManager = connection.getCopyAPI();

        String query = "COPY " + table.getName() +" TO stdout CSV HEADER DELIMITER ','";

        OutputStream outputStream = new FileOutputStream("src\\main\\resources\\tmp\\db.vcs");

        copyManager.copyOut(query, outputStream);
    }

    public void loadTable(Table table) throws SQLException, IOException {
        PGConnection connection = (PGConnection) DriverManager.getConnection(BASE_URL + table.getDatabase().getName(), USER, PASSWORD);
        CopyManager copyManager = connection.getCopyAPI();

        String query = "COPY " + table.getName() +" FROM stdin CSV HEADER DELIMITER ','";

        InputStream inputStream = new FileInputStream("src\\main\\resources\\tmp\\db.vcs");

        copyManager.copyIn(query, inputStream);
    }

    private String createTableQuery(Table table) {
        String query = "CREATE TABLE " + table.getName() + "(\n";

        List<Column> columns = table.getColumns();

        for(int i = 0; i < columns.size() - 1; ++i) {
            Column currentColumn = columns.get(i);

            query = query.concat(currentColumn.getName() + " " + currentColumn.getType() + ",\n");
        }

        query = query.concat(columns.get(columns.size() - 1).getName() + " " + columns.get(columns.size() - 1).getType() + "\n");

        return query.concat(")");
    }

    private String updateTableQuery(Table table, String filterName, String filterValue) {
        String query = "UPDATE " + table.getName() + "\nSET";

        List<Column> columns = table.getColumns();

        for(int i = 0; i < columns.size() - 1; ++i) {
            Column currentColumn = columns.get(i);

            query = query.concat(" " + currentColumn.getName() + " = " + currentColumn.getValue() + ",");
        }

        query = query.concat(" " + columns.get(columns.size() - 1).getName() + " = " + columns.get(columns.size() - 1).getValue() + "\n");

        return query.concat("WHERE " + filterName + " = " + filterValue);
    }


    private Map<String, List<String>> printTable(Table table, ResultSet resultSet) throws SQLException {
        Map<String, List<String>> tableValues = new HashMap<>();

        for(Column column: table.getColumns()) {
            tableValues.put(column.getName(), new ArrayList<>());
        }

        while (resultSet.next()) {
            for(Column currentColumn: table.getColumns()) {

                List<String> currentValue = tableValues.get(currentColumn.getName());
                currentValue.add(resultSet.getString(currentColumn.getName()));

                logger.info("{}: {}", currentColumn.getName(), resultSet.getString(currentColumn.getName()));
            }

            logger.info("================");
        }

        return tableValues;
    }

    private void validate(Table table) throws Exception{
        for(Column currentColumn: table.getColumns()) {

            try {
                DataTypes.valueOf(currentColumn.getType().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                throw new Exception("Unsupported type: " + currentColumn.getType());
            }

        }
    }
}
