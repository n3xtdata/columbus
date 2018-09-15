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

import java.util.Objects;

public class Component {

  private String connectionType;

  private String connectionLabel;

  private String command;

  public Component(String connectionType, String connectionLabel, String command) {

    this.connectionType = connectionType;
    this.connectionLabel = connectionLabel;
    this.command = command;
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

    return "Component{" + "connectionType='" + connectionType + '\'' + ", connectionLabel='" + connectionLabel + '\''
        + ", command='" + command + '\'' + '}';
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
    return Objects.equals(connectionType, component.connectionType) && Objects
        .equals(connectionLabel, component.connectionLabel) && Objects.equals(command, component.command);
  }

  @Override
  public int hashCode() {

    return Objects.hash(connectionType, connectionLabel, command);
  }
}
