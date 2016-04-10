package com.ascendant76.table.core;

public class FailedToAddNewRowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FailedToAddNewRowException() {
    }

    public FailedToAddNewRowException(String message) {
        super(message);
    }

    public FailedToAddNewRowException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToAddNewRowException(Throwable cause) {
        super(cause);
    }

    public FailedToAddNewRowException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
