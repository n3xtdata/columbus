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

import static org.junit.Assert.assertEquals;

import com.n3xtdata.columbus.ColumbusApplicationTests;
import com.n3xtdata.columbus.core.connection.JdbcConnection;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JdbcConnectorServiceTests extends ColumbusApplicationTests {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  JdbcConnectorService jdbcConnectorService;

  @Test
  public void shouldExecuteSqlQuery() throws Exception {

    JdbcConnection jdbcConnection = new JdbcConnection("test-jdbc", null, null,
        "jdbc:sqlite:src/test/resources/jdbc-tests/test.db", "org.sqlite.JDBC",
        "src/test/resources/jdbc-tests/sqllite-jdbc.jar");

    this.jdbcConnectorService.executeDdlDml(jdbcConnection, "DROP TABLE IF EXISTS test_table");
    this.jdbcConnectorService.executeDdlDml(jdbcConnection, "CREATE TABLE test_table(id INTEGER)");
    this.jdbcConnectorService.executeDdlDml(jdbcConnection, "INSERT INTO test_table VALUES (1)");
    this.jdbcConnectorService.executeDdlDml(jdbcConnection, "INSERT INTO test_table VALUES (2)");

    List<Map<String, Object>> result = this.jdbcConnectorService.execute(jdbcConnection, "SELECT * FROM test_table");

    logger.info(result.toString());

    assertEquals(2, result.size());

  }

}
