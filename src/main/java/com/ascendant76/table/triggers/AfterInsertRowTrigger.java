package com.ascendant76.table.triggers;

import com.ascendant76.table.core.Row;

@FunctionalInterface
public interface AfterInsertRowTrigger {
    void after(Row t);
}
