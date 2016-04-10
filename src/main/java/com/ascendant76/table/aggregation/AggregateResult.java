package com.ascendant76.table.aggregation;

import java.math.BigDecimal;

public abstract class AggregateResult {

    public final String name() {
        return AggregateResult.class.getName();
    }

    public abstract AggregateResult add(String value);

    public abstract BigDecimal result();
}
