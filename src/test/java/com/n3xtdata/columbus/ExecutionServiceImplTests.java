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

package com.n3xtdata.columbus;

import static org.mockito.BDDMockito.given;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.Component;
import com.n3xtdata.columbus.core.JdbcConnection;
import com.n3xtdata.columbus.data.MetadataService;
import com.n3xtdata.columbus.executor.ExecutionService;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ExecutionServiceImplTests extends ColumbusApplicationTests {

  @Autowired
  ExecutionService executionService;

  @MockBean
  private MetadataService metadataService;

  @Test
  public void shouldExecuteSqlQuery() throws Exception {

    Component component = new Component("jdbc", "test-jdbc", "SELECT id FROM dummy");

    Set<Component> componentList = new HashSet<>();

    componentList.add(component);

    Check check = new Check("test", "bla", componentList);

    JdbcConnection jdbcConnection = new JdbcConnection("test-jdbc", null, null,
        "jdbc:sqlite:src/test/resources/test.db", "org.sqlite.JDBC", "src/test/resources/sqllite-jdbc.jar");

    given(metadataService.getJdbcConnectionByLabel("test-jdbc")).willReturn(jdbcConnection);

    this.executionService.execute(check);

  }

}
