package com.ascendant76.table.aggregation;

public enum AggregateType {
    AVERAGE {
        @Override
        AggregateResult result() {
            return new AverageResult();
        }
    },
    COUNT {
        @Override
        AggregateResult result() {
            return new CountResult();
        }
    },
    COUNTALL {
        @Override
        AggregateResult result() {
            return new CountStarResult();
        }
    },
    MAX {
        @Override
        AggregateResult result() {
            return new MaxResult();
        }
    },
    MIN {
        @Override
        AggregateResult result() {
            return new MinResult();
        }
    },
    SUM {
        @Override
        AggregateResult result() {
            return new SumResult();
        }
    };

    abstract AggregateResult result();
}
