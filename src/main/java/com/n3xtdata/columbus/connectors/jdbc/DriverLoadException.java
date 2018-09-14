package com.n3xtdata.columbus.connectors.jdbc;

public class DriverLoadException extends Exception {
    public DriverLoadException(String message, Exception cause) {
        super(message, cause);
    }
}