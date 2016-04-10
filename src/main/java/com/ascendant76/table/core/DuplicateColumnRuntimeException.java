package com.ascendant76.table.core;

public class DuplicateColumnRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateColumnRuntimeException() {
    }

    public DuplicateColumnRuntimeException(String message) {
        super(message);
    }

    public DuplicateColumnRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateColumnRuntimeException(Throwable cause) {
        super(cause);
    }

    protected DuplicateColumnRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
