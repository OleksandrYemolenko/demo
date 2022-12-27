package com.example.demo.entities.database;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    private String name;

    private Database database;

    private List<Column> columns = new ArrayList<>();

    private Map<String, List<String>> tableValues = new HashMap<>();
}
