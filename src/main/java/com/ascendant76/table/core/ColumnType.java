package com.ascendant76.table.core;

/**
 * Represents the data type of the data cell. The data cell will be stored as String in a Row. The ColumnType indicates
 * data cell can safely be converted to ColumnType safely when needed.
 */
public enum ColumnType {
    STRING,
    NUMBER,
    BOOLEAN,
    CHAR
}
