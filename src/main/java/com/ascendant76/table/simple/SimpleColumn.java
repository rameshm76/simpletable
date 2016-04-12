package com.ascendant76.table.simple;

import com.ascendant76.table.core.Column;
import com.ascendant76.table.core.ColumnType;
import com.ascendant76.util.Utils;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleColumn implements Column {

    private final ColumnType type;
    private final String name;
    private final String title;

    public SimpleColumn(String name, String title, ColumnType type) {
        checkNotNull(name);
        checkNotNull(title);
        checkNotNull(type);

        this.name = name;
        this.title = title;
        this.type = type;
    }

    public SimpleColumn(String name) {
        this(name, ColumnType.STRING);
    }

    public SimpleColumn(String name, ColumnType type) {
        this(name, Utils.capitalize(name), type);
    }

    @Nonnull
    @Override
    public final String getName() {
        return name;
    }

    @Nonnull
    @Override
    public final String getTitle() {
        return title;
    }

    @Nonnull
    @Override
    public final ColumnType getType() {
        return type;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (null == o || this.getClass() != o.getClass()) {
            return false;
        }
        Column column = (Column) o;
        return this.getType() == column.getType() &&
                Objects.equals(this.getName(), column.getName()) &&
                Objects.equals(this.getTitle(), column.getTitle());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.getType(), this.getName(), this.getTitle());
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", this.getType())
                .add("name", this.getName())
                .add("title", this.getTitle())
                .toString();
    }

}
