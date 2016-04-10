package com.ascendant76.table.simple;

import com.ascendant76.table.core.Row;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class SimpleRowTest {

    @Test
    public void rowConstructionArrayOfStringsTest() {
        String[] cells = {"1", "2.0", "A", "true"};
        Row row = new SimpleRow(cells);
        verify(cells, row);
    }

    @Test
    public void rowConstructionCollectionOfStringsTest() {
        ArrayList<String> cells = Lists.newArrayList("1", "2.0", "A", "true");
        Row row = new SimpleRow(cells);
        verify(cells.toArray(new String[cells.size()]), row);
    }

    @Test
    public void rowConstructionFromRow() {
        ArrayList<String> cells = Lists.newArrayList("1", "2.0", "A", "true");
        Row row = new SimpleRow(new SimpleRow(cells));
        verify(cells.toArray(new String[cells.size()]), row);
    }

    @Test
    public void rowWithArrayOfStringsTest() {
        String[] cells = {"1", "2.0", "A", "true"};
        Row row = new SimpleRow().withCells(cells);
        verify(cells, row);
    }

    @Test
    public void rowWithCollectionOfStringsTest() {
        ArrayList<String> cells = Lists.newArrayList("1", "2.0", "A", "true");
        Row row = new SimpleRow().withCells(cells);
        verify(cells.toArray(new String[cells.size()]), row);
    }

    @Test
    public void rowWithCellTest() {
        ArrayList<String> cells = Lists.newArrayList("1", "2.0", "A");
        Row row = new SimpleRow().withCells(cells).withCell("true");
        verify(Lists.newArrayList("1", "2.0", "A", "true").toArray(new String[cells.size()]), row);
    }

    private void verify(String[] cells, Row row) {
        assertThat(row.getCells(), equalTo(Arrays.asList(cells)));
        assertThat(row.getCellsAsArray(), equalTo(cells));
        assertThat(row.getCell(0), equalTo("1"));
        assertThat(row.getCellAsBoolean(3), equalTo(true));
        assertThat(row.getCellAsChar(2), equalTo('A'));
        assertThat(row.getCellAsInteger(0), equalTo(1));
        assertThat(row.getCellAsDouble(1), equalTo(2.0d));
        assertThat(row.getCellAsFloat(1), equalTo(2.0f));
        assertThat(row.getCellAsLong(0), equalTo(1L));
        assertThat(row.getCellAsShort(0) == 1, is(true));
    }

}