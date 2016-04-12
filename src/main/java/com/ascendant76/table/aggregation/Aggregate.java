package com.ascendant76.table.aggregation;

import com.ascendant76.table.core.ColumnType;
import com.ascendant76.table.core.Row;
import com.ascendant76.table.core.Table;
import com.ascendant76.table.simple.SimpleColumn;
import com.ascendant76.table.simple.SimpleRow;
import com.ascendant76.table.simple.SimpleTable;
import com.ascendant76.util.Utils;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Aggregate {

    private final Table inputTable;
    private final String[] keyColumnNames;
    private final ListMultimap<String, AggregateType> aggregateColumns;

    public Aggregate(Table inputTable, String[] keyColumnNames, ListMultimap<String, AggregateType> aggregateColumns) {
        this.inputTable = inputTable;
        this.keyColumnNames = null == keyColumnNames ? new String[]{} : keyColumnNames;
        this.aggregateColumns = aggregateColumns;
    }

    public final Table getResult() {
        Map<Row, LinkedHashMap<String, AggregateResult>> aggregate = Maps.newLinkedHashMap();

        inputTable.getRows().forEach(currentRow -> {
            Row key = this.getKey(currentRow);
            aggregate.put(key, getValue(currentRow, aggregate.get(key)));
        });

        Table aggregatedTable = new SimpleTable(String.format("%sAggregate", inputTable.getName()));
        Arrays.stream(keyColumnNames).forEach(keyColumnName -> aggregatedTable.addColumn(inputTable.getColumn(keyColumnName)));

        aggregateColumns.entries().stream().forEach(
                entry -> aggregatedTable.addColumn(
                        new SimpleColumn(
                                String.format("%s(%s)", Utils.capitalize(entry.getValue().name()), entry.getKey()),
                                ColumnType.NUMBER
                        )
                )
        );

        aggregate.entrySet().forEach(entry -> {
            entry.getValue().entrySet().forEach(value -> entry.getKey().addCell(value.getValue().result().toPlainString()));
            aggregatedTable.addRow(entry.getKey());
        });

        return aggregatedTable;
    }


    private Row getKey(Row currentRow) {
        Row newRow = new SimpleRow();
        Arrays.stream(keyColumnNames).forEach(keyColumnName -> newRow.addCell(currentRow.getCell(inputTable.getColumnPosition(keyColumnName))));

        return newRow;
    }

    private LinkedHashMap<String, AggregateResult> getValue(Row currentRow, Map<String, AggregateResult> existingValue) {
        LinkedHashMap<String, AggregateResult> result = Maps.newLinkedHashMap();

        aggregateColumns.entries().forEach(entry -> {
            String aggregateColumn = String.format("%s(%s)", Utils.capitalize(entry.getValue().name()), entry.getKey());
            if (null == existingValue) {
                result.put(aggregateColumn, entry.getValue().result().add(currentRow.getCell(inputTable.getColumnPosition(entry.getKey()))));
            } else {
                result.put(aggregateColumn, existingValue.get(aggregateColumn).add(currentRow.getCell(inputTable.getColumnPosition(entry.getKey()))));
            }
        });

        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("inputSimpleTable", inputTable)
                .add("keyColumnNames", keyColumnNames)
                .add("aggregateColumns", aggregateColumns)
                .toString();
    }
}
