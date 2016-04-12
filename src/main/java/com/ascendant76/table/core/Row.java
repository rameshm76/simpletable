package com.ascendant76.table.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * The Row represent data as a composition of cells. For simplicity, we store the information as Strings.
 * <p>
 * The interface holds various categories of methods
 * - Building the row
 * - Query methods about the state of the row
 * - Methods to get the data of the row
 * <p>
 * The getCellAsXXX method returns wrapper types of primitive types. This helps to differentiate the null value and
 * the default values of the primitive types
 */
public interface Row {

    // Building the row
    Row withCells(@Nonnull String... values);

    Row withCells(@Nonnull Collection<String> values);

    Row addCell(@Nullable String value);

    // Query methods about the state of the row
    boolean isEmpty();

    int size();

    // Methods to get the data of the row
    @Nullable
    String getCell(int position);

    @Nullable
    <R> R getCell(int position, Function<String, R> converter);

    @Nonnull
    List<String> getCells();

    @Nonnull
    String[] getCellsAsArray();
}
