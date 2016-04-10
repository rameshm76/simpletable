package com.ascendant76.table.aggregation;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class MinResult extends AggregateResult {
    private BigDecimal min;

    @Override
    public final AggregateResult add(String value) {
        if (null != value) {
            BigDecimal temp = new BigDecimal(value);
            if (null == this.min) {
                min = temp;
            } else {
                min = 0 < this.min.compareTo(temp) ? temp : min;
            }
        }
        return this;
    }

    @Override
    public final BigDecimal result() {
        return min;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("min", min)
                .toString();
    }
}
