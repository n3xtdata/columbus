/*
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

package com.n3xtdata.columbus.loader;

import static org.junit.Assert.assertEquals;

import com.n3xtdata.columbus.ColumbusApplicationTests;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.JdbcConnection;
import com.n3xtdata.columbus.core.SshConnection;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class ColumbusYamlLoaderTests extends ColumbusApplicationTests {

  private HashMap<String, Check> checks;
  private HashMap<String, JdbcConnection> jdbcConnections;
  private HashMap<String, SshConnection> sshConnections;

  private static String COLUMBUS_TEST_HOME = "src/test/resources/yaml-loader-tests";

  @Before
  public void loadYamlFiles() throws Exception {

    ColumbusYamlLoader<Check> checkLoader = new ColumbusYamlLoader<>(Check.class);
    ReflectionTestUtils.setField(checkLoader, "COLUMBUS_HOME", COLUMBUS_TEST_HOME);
    this.checks = checkLoader.load();

    ColumbusYamlLoader<JdbcConnection> jdbcLoader = new ColumbusYamlLoader<>(JdbcConnection.class);
    ReflectionTestUtils.setField(jdbcLoader, "COLUMBUS_HOME", COLUMBUS_TEST_HOME);
    this.jdbcConnections = jdbcLoader.load();

    ColumbusYamlLoader<SshConnection> sshLoader = new ColumbusYamlLoader<>(SshConnection.class);
    ReflectionTestUtils.setField(sshLoader, "COLUMBUS_HOME", COLUMBUS_TEST_HOME);
    this.sshConnections = sshLoader.load();

  }

  @Test
  public void shouldLoadChecks() {
    assertEquals(5, this.checks.size());
  }

  @Test
  public void shouldLoadJdbcConnections() {
    assertEquals(1, this.jdbcConnections.size());
  }

  @Test
  public void shouldLoadSshConnections() {
    assertEquals(1, this.sshConnections.size());
  }







}
