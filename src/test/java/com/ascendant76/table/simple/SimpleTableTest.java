package com.ascendant76.table.simple;

import com.ascendant76.table.aggregation.AggregateType;
import com.ascendant76.table.core.ColumnType;
import com.ascendant76.table.core.Row;
import com.ascendant76.table.core.Table;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;

public class SimpleTableTest {

    static final Row[] SIMPLE_ROWs = {
            new SimpleRow("a1", "b1", "c1", "1"),
            new SimpleRow("a0000000001", "b0000000001", "c0000000001", "1"),
            new SimpleRow("a0000000002", "b0000000002", "c0000000002", "2")
    };

    static Table employee = new SimpleTable("Employee").withColumns("Name", "DeptId").withRows(
            new SimpleRow("Rafferty", "31"),
            new SimpleRow("Jones", "33"),
            new SimpleRow("Heisenberg", "33"),
            new SimpleRow("Robinson", "34"),
            new SimpleRow("Smith", "34"),
            new SimpleRow("Williams", null)
    ).writeAsHtml(new File("/var/tmp", "Employee.html"));

    static Table department = new SimpleTable("Department").withColumns("DeptId", "DepartmentName").withRows(
            new SimpleRow("31", "Sales"),
            new SimpleRow("33", "Engineering"),
            new SimpleRow("34", "Clerical"),
            new SimpleRow("35", "Marketing")
    ).writeAsHtml(new File("/var/tmp", "Department.html"));

    @Test
    public final void emptyTableWillHaveTableDetails() {
        Table emptyTable = new SimpleTable("EmptyTable");
        Assert.assertThat(emptyTable.getColumnNames(), is(not(nullValue())));
    }

    @Test
    public final void tableWithOnlyMetaData() {
        Table emptyTable = new SimpleTable("TableWithOnlyMetaData").withColumns("A", "B", "C", "D");
        Assume.assumeThat(emptyTable, is(not(nullValue())));
        Assert.assertThat(emptyTable.getColumnNames(), hasItems("A", "B", "C", "D"));
    }

    @Test
    public final void tableWithData() {
        Table table = new SimpleTable("tableWithData").withColumns("A", "B", "C", "D").withRows(SIMPLE_ROWs);
        Assert.assertThat(table, is(not(nullValue())));
        Assert.assertThat(table.getColumnNames(), hasItems("A", "B", "C", "D"));
        Assert.assertThat(table.getRows(), hasItems(SIMPLE_ROWs));
    }

    @Test
    public final void tableSelect() {
        Table table = new SimpleTable("tableWithData").withColumns("A", "B", "C", "D").withRows(SIMPLE_ROWs);
        Assert.assertThat(table, is(not(nullValue())));
        Assert.assertThat(table.getColumnNames(), hasItems("A", "B", "C", "D"));
        Assert.assertThat(table.getRows(), hasItems(SIMPLE_ROWs));

        File file = new File("/var/tmp", "tableSelect.html");
        Table newTable = table.select("A").writeAsHtml(file);
        Assert.assertThat(newTable.getColumnNames(), hasItem("A"));
        Assert.assertThat(newTable.getRows().get(0), is(new SimpleRow("a1")));
    }

    @Test
    public final void tableToCsvFile() {
        File csvFile = new File("/var/tmp", "tableToCsvFile.csv");

        Table table = new SimpleTable("tableToCsv")
                .withColumns("column A", "SimpleColumn B", "column c").addColumn(new SimpleColumn("D", ColumnType.NUMBER))
                .withRows(SIMPLE_ROWs)
                .writeAsCsv(csvFile);

        System.out.println(table);

        Table tableFromFile = SimpleTables.fromCsv(csvFile);
        System.out.println(tableFromFile);

        Set<String> expected = table.getColumnNames();
        Set<String> actual = tableFromFile.getColumnNames();
        Assert.assertThat(expected, equalTo(actual));
    }

    @Test
    public final void tableToHtmlFile() {
        File htmlFile = new File("/var/tmp", "tableToHtmlFile.html");
        new SimpleTable("tableToHtml")
                .withColumns("column A", "SimpleColumn B", "column c").addColumn(new SimpleColumn("SimpleColumn D", ColumnType.NUMBER))
                .withRows(SIMPLE_ROWs)
                .writeAsHtml(htmlFile);
        try {
            Assert.assertThat(Files.readFirstLine(htmlFile, Charset.defaultCharset()), is(notNullValue()));
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public final void testAggregate() {
        File htmlFile = new File("/var/tmp", "testAggregate.html");
        Table input = new SimpleTable("Sample").addColumn("Count")
                .addRow(new SimpleRow("1"))
                .addRow(new SimpleRow("2"))
                .addRow(new SimpleRow("3"))
                .addRow(new SimpleRow().addCell(null)).writeAsHtml(htmlFile);

        ListMultimap<String, AggregateType> aggregateColumns = LinkedListMultimap.create();
        aggregateColumns.put("Count", AggregateType.AVERAGE);
        aggregateColumns.put("Count", AggregateType.COUNT);
        aggregateColumns.put("Count", AggregateType.COUNTALL);
        aggregateColumns.put("Count", AggregateType.MAX);
        aggregateColumns.put("Count", AggregateType.MIN);
        aggregateColumns.put("Count", AggregateType.SUM);

        input.aggregate(null, aggregateColumns).writeAsHtml(htmlFile);
    }

    @Test
    public final void testAggregate2() {
        File htmlFile = new File("/var/tmp", "testAggregate2.html");
        Table input = new SimpleTable("testAggregate2").withColumns("Name", "Count")
                .addRow(new SimpleRow("A", "1"))
                .addRow(new SimpleRow("A", "2"))
                .addRow(new SimpleRow("B", "3"))
                .addRow(new SimpleRow("B", null)).writeAsHtml(htmlFile);

        ListMultimap<String, AggregateType> aggregateColumns = LinkedListMultimap.create();
        aggregateColumns.put("Count", AggregateType.AVERAGE);
        aggregateColumns.put("Count", AggregateType.COUNT);
        aggregateColumns.put("Count", AggregateType.COUNTALL);
        aggregateColumns.put("Count", AggregateType.MAX);
        aggregateColumns.put("Count", AggregateType.MIN);
        aggregateColumns.put("Count", AggregateType.SUM);

        input.aggregate(new String[]{"Name"}, aggregateColumns).writeAsHtml(htmlFile);
    }

    @Test
    public void testInnerJoin() {
        SimpleTables.innerJoin(
                employee,
                row -> row.getCell(1),
                department,
                row -> row.getCell(0)
        ).writeAsHtml(new File("/var/tmp", "testInnerJoin.html"));
    }

    @Test
    public void testLeftOuterJoin() {
        SimpleTables.leftOuterJoin(
                employee,
                row -> row.getCell(1),
                department,
                row -> row.getCell(0)
        ).writeAsHtml(new File("/var/tmp", "testLeftOuterJoin.html"));
    }

    @Test
    public void testRightOuterJoin() {
        SimpleTables.rightOuterJoin(
                employee,
                row -> row.getCell(1),
                department,
                row -> row.getCell(0)
        ).writeAsHtml(new File("/var/tmp", "testRightOuterJoin.html"));
    }
}