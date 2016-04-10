package com.ascendant76.table.aggregation;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class SumResult extends AggregateResult {

    private BigDecimal sum = BigDecimal.ZERO;

    @Override
    public final AggregateResult add(String value) {
        if (null != value) {
            sum = sum.add(new BigDecimal(value));
        }
        return this;
    }

    @Override
    public final BigDecimal result() {
        return sum;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sum", sum)
                .toString();
    }
}
