package com.ascendant76.table.simple;

import com.ascendant76.table.aggregation.Aggregate;
import com.ascendant76.table.aggregation.AggregateType;
import com.ascendant76.table.core.*;
import com.ascendant76.table.core.Table;
import com.ascendant76.table.triggers.AfterInsertRowTrigger;
import com.ascendant76.table.triggers.BeforeInsertRowTrigger;
import com.ascendant76.util.Utils;
import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.*;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;


public class SimpleTable implements Table {

    private final AtomicInteger INDEX = new AtomicInteger(0);
    private final String name;
    private final Map<String, Column> columns = Maps.newLinkedHashMap();
    private final BiMap<Integer, String> indexToColumnName = HashBiMap.create();
    private final ArrayList<Row> rows;
    private ArrayList<BeforeInsertRowTrigger> beforeInsertRowTriggers;
    private ArrayList<AfterInsertRowTrigger> afterInsertRowTriggers;

    public SimpleTable(String name) {
        this.name = Utils.capitalize(name);
        rows = Lists.newArrayList();
        beforeInsertRowTriggers = Lists.newArrayList();
        afterInsertRowTriggers = Lists.newArrayList();
    }

    @Override
    public final Table withColumns(Column... columns) {
        checkNotNull(columns);
        Arrays.stream(columns).forEach(this::addColumn);
        return this;
    }

    @Override
    public final Table withColumns(Collection<Column> columns) {
        checkNotNull(columns);
        columns.forEach(this::addColumn);
        return this;
    }

    @Override
    public final Table addColumn(@Nullable Column column) {
        if (columns.containsKey(column.getName())) {
            throw new DuplicateColumnRuntimeException(String.format("SimpleColumn %s already exist", column.getName()));
        }
        indexToColumnName.put(INDEX.getAndIncrement(), column.getName());
        columns.put(column.getName(), column);

        return this;
    }

    @Override
    public final Table addColumn(String columnName) {
        checkNotNull(columnName);
        return this.addColumn(new SimpleColumn(columnName));
    }

    @Override
    public final Table withColumns(String... columnNames) {
        checkNotNull(columnNames);
        Arrays.stream(columnNames).forEach(this::addColumn);
        return this;
    }

    @Override
    public final Table addRow(Row row) {
        checkNotNull(row);
        Preconditions.checkState(INDEX.get() == row.size());
        beforeInsertRowTriggers.forEach(beforeInsertRowTrigger -> beforeInsertRowTrigger.before(row));
        boolean status = rows.add(new SimpleRow(row));
        if (!status) {
            throw new FailedToAddNewRowException();
        }
        afterInsertRowTriggers.forEach(afterInsertRowTrigger -> afterInsertRowTrigger.after(row));
        return this;
    }

    @Override
    public final Table withRows(Row... rows) {
        checkNotNull(rows);
        Arrays.stream(rows).forEach(this::addRow);
        return this;
    }

    @Override
    public final Table withRows(Collection<Row> rows) {
        checkNotNull(rows);
        rows.forEach(this::addRow);
        return this;
    }

    @Nonnull
    @Override
    public Table addBeforeInsertRowTriggers(BeforeInsertRowTrigger... beforeInsertRowTriggers) {
        this.beforeInsertRowTriggers = Lists.newArrayList(beforeInsertRowTriggers);
        return this;
    }

    @Nonnull
    @Override
    public Table addAfterInsertRowTriggers(AfterInsertRowTrigger... afterInsertRowTriggers) {
        this.afterInsertRowTriggers = Lists.newArrayList(afterInsertRowTriggers);
        return this;
    }

    // Utility Methods
    @Override
    public final Table select(String... columnNames) {
        Table newTable = new SimpleTable(name);
        for (String columnName : columnNames) {
            newTable.addColumn(this.getColumn(columnName));
        }

        for (Row row : rows) {
            Row newRow = new SimpleRow();
            for (String columnName : columnNames) {
                newRow.withCell(row.getCell(this.getColumnPosition(columnName)));
            }
            newTable.addRow(newRow);
        }
        return newTable;
    }

    @Override
    public final Table select(List<String> columnNames, Map<String, String> columnAliases) {
        Table newTable = new SimpleTable(name);
        for (String columnName : columnNames) {
            Column temp = this.columns.get(columnName);
            newTable.addColumn(new SimpleColumn(temp.getName(), temp.getTitle(), temp.getType()));
        }

        for (Row row : rows) {
            Row newRow = new SimpleRow();
            for (String columnName : columnNames) {
                newRow.withCell(row.getCell(this.getColumnPosition(columnName)));
            }
            newTable.addRow(newRow);
        }
        return newTable;
    }

    @Override
    public final Table filter(Predicate<Row> filters) {
        Table newTable = new SimpleTable(name);
        newTable.withColumns(this.getColumns().toArray(new Column[this.getColumns().size()]));
        newTable.withRows(rows.toArray(new Row[rows.size()]));
        return newTable;
    }

    @Override
    public final Table parition(int startPosition, int endPosition) {
        Table newTable = new SimpleTable(name);
        newTable.withColumns(this.getColumns().toArray(new Column[this.getColumns().size()]));
        newTable.withRows(rows.subList(startPosition, endPosition));
        return newTable;
    }

    @Override
    public final Table aggregate(String[] keyColumnNames, ListMultimap<String, AggregateType> aggregateColumns) {
        return new Aggregate(this, keyColumnNames, aggregateColumns).getResult();
    }

    @Override
    public final Pair<Table, Table> split(Table table, Predicate<Row> rowPredicate) {
        Table left = new SimpleTable(this.name).withColumns(this.getColumns());
        Table right = new SimpleTable(this.name).withColumns(this.getColumns());

        for (Row row : this.getRows()) {
            if (rowPredicate.test(row)) {
                left.addRow(row);
            } else {
                right.addRow(row);
            }
        }

        return Pair.of(left, right);
    }

    @Override
    public Table writeAsCsv(File file) {
        checkNotNull(file);
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8.name()))) {
            Set<String> columnNamesSet = this.getColumnNames();
            writer.writeNext(columnNamesSet.toArray(new String[columnNamesSet.size()]));
            for (Row row : rows) {
                writer.writeNext(row.getCellsAsArray());
            }
        } catch (IOException ioe) {
            Throwables.propagateIfPossible(ioe);
        }

        return this;
    }

    @Override
    public Table writeAsHtml(File file) {
        checkNotNull(file);

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8.name())) {
            writer.write("<style>");
            writer.write("* { font: Helvetica, arial, nimbussansl, liberationsans, freesans, clean, sans-serif, \"Apple Color Emoji\", \"Segoe UI Emoji\", \"Segoe UI Symbol\"; }\n");
            writer.write("table { border-collapse: collapse; border-spacing: 0; font: 13px / 1.4; }\n");
            writer.write("th { border: 1px solid #ddd; display: table-cell; text-align: center }\n");
            writer.write("td { border: 1px solid #ddd; display: table-cell; }\n");
            writer.write(".number { text-align: right}\n");
            writer.write(".boolean { text-align: center}\n");
            writer.write(".char { text-align: center}\n");
            writer.write("</style>");
            writer.write(String.format("<h2><u>%s</u></h2>", name));
            writer.write("<table border=\"1\">");
            writer.write("<tr>");
            for (Column column : this.getColumns()) {
                writer.write(String.format("<th title=\"%s\">%s</th>", column.getName(), column.getTitle()));
            }
            writer.write("</tr>");

            for (Row row : this.getRows()) {
                writer.write("<tr>");
                for (int i = 0; i < row.getCells().size(); i++) {
                    writer.write(String.format("<td class=\"%s\">%s</td>", this.getColumn(i).getType().name().toLowerCase(), row.getCells().get(i)));
                }
                writer.write("</tr>");
            }
            writer.write("</tr>");
            writer.write("</table>");
        } catch (IOException ioe) {
            Throwables.propagateIfPossible(ioe);
        }
        return this;
    }

    @Override
    public final Table truncate() {
        this.getRows().clear();
        return this;
    }

    @Override
    public final Table delete() {
        INDEX.set(0);
        columns.clear();
        indexToColumnName.clear();
        rows.clear();

        return this;
    }

    // Terminal methods
    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Set<Column> getColumns() {
        return ImmutableSet.copyOf(columns.values());
    }

    @Override
    public final Column getColumn(int position) {
        return columns.get(indexToColumnName.get(position));
    }

    @Override
    public final Column getColumn(String name) {
        return columns.get(name);
    }

    @Override
    public final int getColumnPosition(String name) {
        return indexToColumnName.inverse().get(name);
    }

    @Override
    public final Set<String> getColumnNames() {
        return columns.keySet();
    }

    @Override
    public final List<Row> getRows() {
        return ImmutableList.copyOf(rows);
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("columns", columns)
                .add("simpleRows", rows)
                .toString();
    }
}
