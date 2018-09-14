package com.n3xtdata.columbus.connectors.jdbc;

public class QueryExecutionException extends Exception {
    public QueryExecutionException(String message, Exception cause) {
        super(message, cause);
    }
}