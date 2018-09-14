package com.n3xtdata.columbus.connectors.jdbc;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class JdbcQueryServiceImpl implements JdbcQueryService {


    public List<Map<String, Object>> execute(JdbcConnection connection, String query) throws IOException, QueryExecutionException {

        final List<Map<String, Object>> queryResult = new ArrayList<>();

        RowCallbackHandler handler = resultSet -> queryResult.addAll(JdbcResultSetConverter.convertResultSet(resultSet));

        connection.executeQuery(query, handler);

        return queryResult;

    }


}
