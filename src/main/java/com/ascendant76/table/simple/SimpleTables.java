package com.ascendant76.table.simple;

import com.ascendant76.table.core.Row;
import com.ascendant76.table.core.Table;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

public class SimpleTables {

    public static Table fromCsv(File csvFile) {
        Table table = new SimpleTable(csvFile.getName());
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), Charsets.UTF_8.name()))) {
            table.withColumns(csvReader.readNext());
            for (String[] cells : csvReader) {
                table.addRow(new SimpleRow(cells));
            }
        } catch (Exception e) {
            Throwables.propagateIfPossible(e);
        }
        return table;
    }

    public static Table innerjoin(Table left, Table right, Function<String, Row> leftKeyGenerator, Function<String, Row> rightKeyGenerator) {
        Table result = new SimpleTable(String.format("%s_%s", left.getName(), right.getName())).withColumns(left.getColumns()).withColumns(right.getColumns());
        return result;
    }

    public static Table leftJoin(Table left, Table right, Function<String, Row> leftKeyGenerator, Function<String, Row> rightKeyGenerator) {
        return null;
    }

    public static Table rightJoin(Table left, Table right, Function<String, Row> leftKeyGenerator, Function<String, Row> rightKeyGenerator) {
        return null;
    }

    public static Table fullOuterJoin(Table left, Table right, Function<String, Row> leftKeyGenerator, Function<String, Row> rightKeyGenerator) {
        Table result = new SimpleTable(String.format("%s_%s", left.getName(), right.getName())).withColumns(left.getColumns()).withColumns(right.getColumns());
        return result;
    }
}