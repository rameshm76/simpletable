package com.ascendant76.table.simple;

import com.ascendant76.table.core.Row;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
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
        Row row = new SimpleRow().withCells(cells).addCell("true");
        verify(Lists.newArrayList("1", "2.0", "A", "true").toArray(new String[cells.size()]), row);
    }

    private void verify(String[] cells, Row row) {
        assertThat(row.getCells(), equalTo(Arrays.asList(cells)));
        assertThat(row.getCellsAsArray(), equalTo(cells));
        assertThat(row.getCell(0), equalTo("1"));
        assertThat(row.getCell(3, BooleanUtils::toBoolean), equalTo(true));
        assertThat(row.getCell(2, CharUtils::toChar), equalTo('A'));
        assertThat(row.getCell(0, NumberUtils::toInt), equalTo(1));
        assertThat(row.getCell(1, NumberUtils::toDouble), equalTo(2.0d));
        assertThat(row.getCell(1, NumberUtils::toFloat), equalTo(2.0f));
        assertThat(row.getCell(0, NumberUtils::toLong), equalTo(1L));
        assertThat(row.getCell(0, NumberUtils::toShort), is(Short.valueOf("1")));
    }

}