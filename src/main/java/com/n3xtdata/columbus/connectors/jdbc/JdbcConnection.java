/**
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.n3xtdata.columbus.connectors.jdbc;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Driver;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class JdbcConnection {

  private String label;

  private String username;

  private String password;

  private String url;

  private String driverClass;

  private String driverPath;

  private byte[] jdbcDriverJar;

  public JdbcConnection() {
  }

  public JdbcConnection(String label, String username, String password, String url, String driverClass, String driverPath)
      throws IOException {
    this.label = label;
    this.username = username;
    this.password = password;
    this.url = url;
    this.driverClass = driverClass;
    setDriverPath(driverPath);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDriverClass() {
    return driverClass;
  }

  public String getDriverPath() {
    return driverPath;
  }

  public void setDriverPath(String driverPath) throws IOException {
    this.jdbcDriverJar = loadJdbcDriverJar(driverPath);
    this.driverPath = driverPath;
  }





  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  public byte[] getJdbcDriverJar() {
    return jdbcDriverJar;
  }


  @Override
  public String toString() {
    return "JdbcConnection{" +
        "label='" + label + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", url='" + url + '\'' +
        ", driverClass='" + driverClass + '\'' +
        ", driverPath='" + driverPath + '\'' +
        '}';
  }

  public void executeQuery(String query, RowCallbackHandler handler) throws QueryExecutionException {
    try {
      JdbcTemplate template = getJdbcTemplateFromConnection();
      template.query(query, handler);
    } catch (Exception e) {
      throw new QueryExecutionException("unable to execute query", e);
    }
  }

  private byte[] loadJdbcDriverJar(String inputPath) throws IOException {
    Path path = Paths.get(inputPath);

    byte[] jdbcDriverJar = Files.readAllBytes(path);

    return jdbcDriverJar;
  }

  private JdbcTemplate getJdbcTemplateFromConnection() throws DriverLoadException {
    return new JdbcTemplate(createDataSourceFromEntity());
  }

  private DataSource createDataSourceFromEntity() throws DriverLoadException {
    byte[] jdbcDriverJar = getJdbcDriverJar();
    String connectorClass = getDriverClass();
    String url = getUrl();

    DriverFactory driverFactory = new DriverFactory();


    Driver driver = driverFactory.createDriverFromJar(connectorClass, jdbcDriverJar);

    SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, url);
    dataSource.setPassword(getPassword());
    dataSource.setUsername(getUsername());

    return dataSource;
  }


}
