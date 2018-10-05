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

package com.n3xtdata.columbus.core;


import com.n3xtdata.columbus.config.SpringContext;
import com.n3xtdata.columbus.data.MetadataService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class JdbcComponentDetails implements ComponentDetails {

  private static final ApplicationContext context = SpringContext.getAppContext();
  private static final MetadataService metadataService = (MetadataService) context.getBean("metadataServiceImpl");

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String connectionLabel;

  private String command;


  public JdbcComponentDetails(HashMap<String, Object> map) {

    this.connectionLabel = (String) map.get("connection");
    this.command = (String) map.get("sqlQuery");
  }

  @Override
  public List<Map<String, Object>> execute() throws Exception {

    logger.info(this.command + " + " + this.connectionLabel);
      logger.info("NOT FOUND: " + metadataService);

    Connection connection = metadataService.getConnectionByLabel(this.connectionLabel);

    return connection.execute(this.command);
  }

  public String getConnectionLabel() {

    return connectionLabel;
  }

  public void setConnectionLabel(String connectionLabel) {

    this.connectionLabel = connectionLabel;
  }

  public String getCommand() {

    return command;
  }

  public void setCommand(String command) {

    this.command = command;
  }
}
