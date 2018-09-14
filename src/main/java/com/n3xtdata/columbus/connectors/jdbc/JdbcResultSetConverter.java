package com.n3xtdata.columbus.connectors.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcResultSetConverter {

    public static List<Map<String, Object>> convertResultSet(ResultSet resultSet) throws SQLException {

        int columnCount = resultSet.getMetaData().getColumnCount();

        List<Map<String, Object>> queryResult = new ArrayList<>();


        do {

            Map<String, Object> row = new HashMap<>();

            for (int i = 1; i <= columnCount; i++) {

                row.put(resultSet.getMetaData().getColumnLabel(i).toLowerCase(), resultSet.getObject(i));

            }

            queryResult.add(row);

        }
        while (resultSet.next());


        return queryResult;

    }

}