package com.ascendant76.table.simple;

import com.ascendant76.table.core.Row;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleRow implements Row {

    private final List<String> cells;

    public SimpleRow() {
        cells = Lists.newArrayList();
    }

    public SimpleRow(String... values) {
        checkNotNull(values);
        cells = Lists.newArrayList(values);
    }

    public SimpleRow(Collection<String> values) {
        checkNotNull(values);
        cells = Lists.newArrayList(values);
    }

    public SimpleRow(Row row) {
        checkNotNull(row);
        cells = Lists.newArrayList(row.getCells());
    }

    @Override
    public final Row withCells(String... values) {
        checkNotNull(values);
        boolean status = Collections.addAll(this.getCells(), values);
        Preconditions.checkArgument(status);
        return this;
    }

    @Override
    public final Row withCells(Collection<String> values) {
        checkNotNull(values);
        boolean status = this.getCells().addAll(values);
        Preconditions.checkArgument(status);
        return this;
    }

    @Override
    public final Row withCell(String value) {
        boolean status = this.getCells().add(value);
        Preconditions.checkArgument(status);
        return this;
    }

    @Override
    public final int size() {
        return this.getCells().size();
    }

    @Override
    public boolean isEmpty() {
        return this.cells.isEmpty();
    }

    @Override
    public final String getCell(int position) {
        return this.getCells().get(position);
    }

    @Override
    public Integer getCellAsInteger(int position) {
        return NumberUtils.toInt(this.getCell(position));
    }

    @Override
    public Long getCellAsLong(int position) {
        return NumberUtils.toLong(this.getCell(position));
    }

    @Override
    public Short getCellAsShort(int position) {
        return NumberUtils.toShort(this.getCell(position));
    }

    @Override
    public Double getCellAsDouble(int position) {
        return NumberUtils.toDouble(this.getCell(position));
    }

    @Override
    public Float getCellAsFloat(int position) {
        return NumberUtils.toFloat(this.getCell(position));
    }

    @Override
    public Boolean getCellAsBoolean(int position) {
        return BooleanUtils.toBoolean(this.getCell(position));
    }

    @Override
    public Character getCellAsChar(int position) {
        return CharUtils.toChar(this.cells.get(position));
    }

    @Override
    public final List<String> getCells() {
        return cells;
    }

    @Override
    public final String[] getCellsAsArray() {
        return this.getCells().toArray(new String[this.getCells().size()]);
    }


    @Override
    public final int hashCode() {
        return Objects.hash(this.getCells());
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (null == obj || this.getClass() != obj.getClass()) {
            return false;
        }
        Row other = (Row) obj;
        return Objects.equals(getCells(), other.getCells());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cells", this.getCells())
                .toString();
    }
}
