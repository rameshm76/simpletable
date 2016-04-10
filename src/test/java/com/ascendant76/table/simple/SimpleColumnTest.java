package com.ascendant76.table.simple;

import com.ascendant76.table.core.ColumnType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleColumnTest {

    @Test
    public void simpleColumnCreatedWithColumnNameTest() {
        SimpleColumn sc = new SimpleColumn("A");
        assertThat(sc.getName(), is("A"));
        assertThat(sc.getName(), is(sc.getTitle()));
        assertThat(sc.getType(), is(ColumnType.STRING));
    }

    @Test
    public void simpleColumnCreatedWithColumnNameAndColumnTypeTest() {
        SimpleColumn sc = new SimpleColumn("A", ColumnType.NUMBER);
        assertThat(sc.getName(), is("A"));
        assertThat(sc.getName(), is(sc.getTitle()));
        assertThat(sc.getType(), is(ColumnType.NUMBER));
    }

    @Test
    public void simpleColumnCreatedWithColumnNameTitleAndColumnTypeTest() {
        SimpleColumn sc = new SimpleColumn("A", "Column A", ColumnType.NUMBER);
        assertThat(sc.getName(), is("A"));
        assertThat(sc.getTitle(), is("Column A"));
        assertThat(sc.getType(), is(ColumnType.NUMBER));
    }
}