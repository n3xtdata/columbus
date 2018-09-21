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

package com.n3xtdata.columbus.core;

import com.n3xtdata.columbus.connectors.jdbc.JdbcConnectorService;
import com.n3xtdata.columbus.data.MetadataService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String label;

  private String connectionType;

  private String connectionLabel;

  private String command;

  @SuppressWarnings({"unused"})
  public Component() {

  }

  @SuppressWarnings({"unused"})
  public Component(String label, String connectionType, String connectionLabel, String command) {
    this.label = label;
    this.connectionType = connectionType;
    this.connectionLabel = connectionLabel;
    this.command = command;
  }

  @SuppressWarnings({"unused"})
  public void setLabel(String label) {
    this.label = label;
  }

  @SuppressWarnings({"unused"})
  public String getConnectionType() {

    return connectionType;
  }

  @SuppressWarnings({"unused"})
  public void setConnectionType(String connectionType) {

    this.connectionType = connectionType;
  }

  @SuppressWarnings({"unused"})
  public String getConnectionLabel() {

    return connectionLabel;
  }

  @SuppressWarnings({"unused"})
  public void setConnectionLabel(String connectionLabel) {

    this.connectionLabel = connectionLabel;
  }

  @SuppressWarnings({"unused"})
  public String getCommand() {

    return command;
  }

  @SuppressWarnings({"unused"})
  public void setCommand(String command) {

    this.command = command;
  }

  @Override
  public String toString() {
    return "Component{" +
        "label='" + label + '\'' +
        ", connectionType='" + connectionType + '\'' +
        ", connectionLabel='" + connectionLabel + '\'' +
        ", command='" + command + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Component component = (Component) o;
    return Objects.equals(label, component.label) &&
        Objects.equals(connectionType, component.connectionType) &&
        Objects.equals(connectionLabel, component.connectionLabel) &&
        Objects.equals(command, component.command);
  }

  @Override
  public int hashCode() {

    return Objects.hash(label, connectionType, connectionLabel, command);
  }

  public void execute(Map<String, List<Map<String, Object>>> results, MetadataService metadataService,
      JdbcConnectorService jdbcConnectorService) throws Exception {
    switch (this.getConnectionType()) {
      case "jdbc":
        results.put(this.label, this.executeJdbcConnector(metadataService, jdbcConnectorService));
        break;
      case "ssh":
        break;
      default:
        break;
    }
  }

  private List<Map<String, Object>> executeJdbcConnector(MetadataService metadataService,
      JdbcConnectorService jdbcConnectorService) throws Exception {

    logger.info(this.getCommand());

    JdbcConnection connection = metadataService.getJdbcConnectionByLabel(this.getConnectionLabel());
    List<Map<String, Object>> result = jdbcConnectorService.execute(connection, this.getCommand());

    logger.info(result.toString());

    return result;
  }
}
