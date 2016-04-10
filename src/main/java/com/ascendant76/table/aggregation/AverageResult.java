package com.ascendant76.table.aggregation;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AverageResult extends AggregateResult {

    private int counter;
    private BigDecimal sum = BigDecimal.ZERO;

    @Override
    public final AggregateResult add(String value) {
        if (null != value) {
            counter = counter + 1;
            sum = sum.add(new BigDecimal(value));
        }
        return this;
    }

    @Override
    public final BigDecimal result() {
        return sum.divide(new BigDecimal(counter), RoundingMode.UP);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("counter", counter)
                .add("sum", sum)
                .toString();
    }
}
