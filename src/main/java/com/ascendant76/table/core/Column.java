package com.ascendant76.table.core;

import javax.annotation.Nonnull;

/**
 * The Column represents the meta information of the data cell.
 */
public interface Column {
    @Nonnull
    String getName();

    @Nonnull
    String getTitle();

    @Nonnull
    ColumnType getType();
}
