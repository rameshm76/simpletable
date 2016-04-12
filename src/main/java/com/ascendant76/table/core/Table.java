package com.ascendant76.table.core;

import com.ascendant76.table.aggregation.AggregateType;
import com.ascendant76.table.triggers.AfterInsertRowTrigger;
import com.ascendant76.table.triggers.BeforeInsertRowTrigger;
import com.google.common.collect.ListMultimap;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Table represents the structure of two dimensional data. The rows holds the data and, the columns describe the type of
 * data in the cells of a row.
 * <p>
 * The interface holds various categories of methods
 * - Building the table
 * - TODO: Need to add configurations to enable trimming of blank spaces
 * - Triggers that can be invoked on pre/post modifications of a row
 * - Utility methods to work on the data of the table
 * - Utility methods to store the table to file
 * - TODO: Create a method to writeAsAsciiTable
 * - Non-chained methods which returns the meta data of the table
 */
public interface Table {

    // Building the table columns and rows
    @Nonnull
    Table withColumns(@Nonnull Column... columns);

    @Nonnull
    Table withColumns(@Nonnull Collection<Column> columns);

    @Nonnull
    Table withColumns(@Nonnull String... columnNames);

    @Nonnull
    Table withRows(@Nonnull Row... rows);

    @Nonnull
    Table withRows(@Nonnull Collection<Row> rows);

    @Nonnull
    Table addColumn(@Nonnull String columnName);

    @Nonnull
    Table addColumn(@Nonnull Column column);

    @Nonnull
    Table addRow(@Nonnull Row row);

    @Nonnull
    Table addBeforeInsertRowTriggers(@Nonnull BeforeInsertRowTrigger... beforeInsertRowTrigger);

    @Nonnull
    Table addAfterInsertRowTriggers(@Nonnull AfterInsertRowTrigger... afterInsertRowTrigger);

    // Utility methods to work on the data of the table
    @Nonnull
    Table select(@Nonnull String... columnNames);

    @Nonnull
    Table select(@Nonnull List<String> columnNames, @Nonnull Map<String, String> columnAliases);

    @Nonnull
    Table filter(@Nonnull Predicate<Row> filters);

    @Nonnull
    Table parition(int startPosition, int endPosition);

    @Nonnull
    Pair<Table, Table> split(@Nonnull Table table, @Nonnull Predicate<Row> rowPredicate);

    @Nonnull
    Table aggregate(@Nullable String[] keyColumnNames, @Nonnull ListMultimap<String, AggregateType> aggregateColumns);

    // Utility methods to store the table to file
    @Nonnull
    Table writeAsCsv(@Nonnull File file);

    @Nonnull
    Table writeAsHtml(@Nonnull File file);

    /**
     * Clears away the data. Any references to the table instance will be affected by this operation.
     *
     * @return the current table with no data
     */
    @Nonnull
    Table truncate();

    /**
     * Deletes all the rows and columns information fromCsv the table. The name of the table will remain.
     *
     * @return an empty table with no row or column information
     */
    @Nonnull
    Table delete();

    // Terminal methods
    @Nonnull
    String getName();

    @Nonnull
    Set<Column> getColumns();

    boolean hasColumn(@Nonnull String name);

    @Nonnull
    Column getColumn(int position);

    @Nonnull
    Column getColumn(@Nonnull String name);

    int getColumnPosition(@Nonnull String name);

    @Nonnull
    Set<String> getColumnNames();

    @Nonnull
    List<Row> getRows();

    default int getTableSize() {
        return this.getRows().size();
    }
}
