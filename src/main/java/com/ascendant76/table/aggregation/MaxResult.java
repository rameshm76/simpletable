package com.ascendant76.table.aggregation;

import com.google.common.base.MoreObjects;

import java.math.BigDecimal;

public class MaxResult extends AggregateResult {

    private BigDecimal max;

    @Override
    public final AggregateResult add(String value) {
        if (null != value) {
            BigDecimal temp = new BigDecimal(value);
            if (null == this.max) {
                max = temp;
            } else {
                max = 0 > this.max.compareTo(temp) ? temp : max;
            }
        }
        return this;
    }

    @Override
    public final BigDecimal result() {
        return max;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("max", max)
                .toString();
    }
}
