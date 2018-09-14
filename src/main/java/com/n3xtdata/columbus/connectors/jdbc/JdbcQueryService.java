package com.n3xtdata.columbus.connectors.jdbc;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JdbcQueryService {

    List<Map<String, Object>> execute(JdbcConnection connection, String query) throws IOException, QueryExecutionException;
}
