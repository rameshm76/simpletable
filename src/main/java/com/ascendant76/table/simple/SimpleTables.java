package com.ascendant76.table.simple;

import com.ascendant76.table.core.Column;
import com.ascendant76.table.core.Row;
import com.ascendant76.table.core.Table;
import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.LinkedHashSet;
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

    public static Table innerJoin(Table left, Function<Row, String> leftKeyGenerator, Table right, Function<Row, String> rightKeyGenerator) {

        LinkedHashSet<Column> allColumns = Sets.newLinkedHashSet(left.getColumns());
        allColumns.addAll(right.getColumns());

        Table result = new SimpleTable(String.format("%s_%s", left.getName(), right.getName())).withColumns(allColumns);

        Multimap<String, Row> data = LinkedListMultimap.create();
        left.getRows().forEach((Row leftRow) -> {
            String key = leftKeyGenerator.apply(leftRow);
            data.put(key, leftRow);
        });

        right.getRows().forEach(rightRow -> {
            String key = rightKeyGenerator.apply(rightRow);
            Collection<Row> leftRows = data.get(key);
            if (leftRows != null) {
                for (Row leftRow : leftRows) {
                    Row newRow = new SimpleRow();
                    result.getColumnNames().forEach(columnName -> {
                        if (left.hasColumn(columnName)) {
                            newRow.addCell(leftRow.getCell(left.getColumnPosition(columnName)));
                        } else {
                            newRow.addCell(rightRow.getCell(right.getColumnPosition(columnName)));
                        }
                    });
                    result.addRow(newRow);
                }
            }
        });
        return result;
    }


    public static Table leftOuterJoin(Table left, Function<Row, String> leftKeyGenerator, Table right, Function<Row, String> rightKeyGenerator) {
        LinkedHashSet<Column> allColumns = Sets.newLinkedHashSet(left.getColumns());
        allColumns.addAll(right.getColumns());

        Table result = new SimpleTable(String.format("%s_%s", left.getName(), right.getName())).withColumns(allColumns);

        Multimap<String, Row> data = LinkedListMultimap.create();
        right.getRows().forEach((Row rightRow) -> {
            String key = rightKeyGenerator.apply(rightRow);
            data.put(key, rightRow);
        });

        left.getRows().forEach(leftRow -> {
            String key = leftKeyGenerator.apply(leftRow);
            Collection<Row> rightRows = data.get(key);
            System.out.println(rightRows);
            if (rightRows == null || rightRows.isEmpty()) {
                Row newRow = new SimpleRow();
                result.getColumnNames().forEach(columnName -> {
                    if (left.hasColumn(columnName)) {
                        newRow.addCell(leftRow.getCell(left.getColumnPosition(columnName)));
                    } else {
                        newRow.addCell(null);
                    }
                });
                result.addRow(newRow);
            } else {
                for (Row rightRow : rightRows) {
                    Row newRow = new SimpleRow();
                    result.getColumnNames().forEach(columnName -> {
                        if (left.hasColumn(columnName)) {
                            newRow.addCell(leftRow.getCell(left.getColumnPosition(columnName)));
                        } else {
                            newRow.addCell(rightRow.getCell(right.getColumnPosition(columnName)));
                        }
                    });
                    result.addRow(newRow);
                }
            }
        });

        return result;
    }

    public static Table rightOuterJoin(Table left, Function<Row, String> leftKeyGenerator, Table right, Function<Row, String> rightKeyGenerator) {
        return leftOuterJoin(right, rightKeyGenerator, left, leftKeyGenerator);
    }
}