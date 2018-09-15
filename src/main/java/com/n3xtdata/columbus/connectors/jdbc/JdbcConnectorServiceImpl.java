/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.n3xtdata.columbus.connectors.jdbc;

import com.n3xtdata.columbus.core.JdbcConnection;
import java.sql.Driver;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

@Service
public class JdbcConnectorServiceImpl implements JdbcConnectorService {


  public List<Map<String, Object>> execute(JdbcConnection jdbcConnection, String query) throws QueryExecutionException {

    List<Map<String, Object>> queryResult;

    try {
      JdbcTemplate template = getJdbcTemplateFromConnection(jdbcConnection);
      queryResult = template.queryForList(query);
    } catch (Exception e) {
      throw new QueryExecutionException("unable to execute query", e);
    }

    return queryResult;

  }

  public void executeDdlDml(JdbcConnection jdbcConnection, String query) throws QueryExecutionException {

    try {
      JdbcTemplate template = getJdbcTemplateFromConnection(jdbcConnection);
      template.execute(query);
    } catch (Exception e) {
      throw new QueryExecutionException("unable to execute query", e);
    }

  }


  private JdbcTemplate getJdbcTemplateFromConnection(JdbcConnection jdbcConnection) throws DriverLoadException {

    return new JdbcTemplate(createDataSourceFromEntity(jdbcConnection));
  }

  private DataSource createDataSourceFromEntity(JdbcConnection jdbcConnection) throws DriverLoadException {

    byte[] jdbcDriverJar = jdbcConnection.getDriverJar();
    String connectorClass = jdbcConnection.getDriverClass();
    String url = jdbcConnection.getUrl();

    DriverFactory driverFactory = new DriverFactory();

    Driver driver = driverFactory.createDriverFromJar(connectorClass, jdbcDriverJar);

    SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url);
    dataSource.setPassword(jdbcConnection.getPassword());
    dataSource.setUsername(jdbcConnection.getUsername());

    System.out.println(dataSource.toString());

    return dataSource;
  }


}
