/**
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

package com.n3xtdata.columbus.executor;

import com.n3xtdata.columbus.connectors.jdbc.JdbcConnection;
import com.n3xtdata.columbus.connectors.jdbc.JdbcQueryService;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.Component;
import com.n3xtdata.columbus.data.MetadataService;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

  private MetadataService metadataService;

  private JdbcQueryService jdbcQueryService;

  public ExecutionServiceImpl(MetadataService metadataService, JdbcQueryService jdbcQueryService) {
    this.metadataService = metadataService;
    this.jdbcQueryService = jdbcQueryService;
  }

  @Override
  public void execute(Check check) {

    check.getComponents().forEach(component -> {

      switch (component.getConnectionType()) {
        case "jdbc":
          try {
            this.executeJdbcConnector(component);
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        case "ssh":

          break;
        default:

          break;
      }

    });


  }

  private void executeJdbcConnector(Component component) throws Exception {

    System.out.println(component.getCommand());

    JdbcConnection connection = this.metadataService
        .getJdbcConnectionByLabel(component.getConnectionLabel());

    List<Map<String, Object>> result = jdbcQueryService.execute(connection, component.getCommand());

    System.out.println(result.toString());

  }


}
