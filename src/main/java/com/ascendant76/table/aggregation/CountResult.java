package com.ascendant76.table.aggregation;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class CountResult extends AggregateResult {

    private int count;

    @Override
    public final AggregateResult add(String value) {
        if (null != value) {
            count++;
        }
        return this;
    }

    @Override
    public final BigDecimal result() {
        return new BigDecimal(count);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("count", count)
                .toString();
    }
}
