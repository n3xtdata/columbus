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

package com.n3xtdata.columbus.core;

public class Component {

  private String connectionType;

  private String connectionLabel;

  private String command;


  public Component() {
  }

  public Component(String connectionType, String connectionLabel, String command) {
    this.connectionType = connectionType;
    this.connectionLabel = connectionLabel;
    this.command = command;
  }

  public String getConnectionType() {
    return connectionType;
  }

  public void setConnectionType(String connectionType) {
    this.connectionType = connectionType;
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

  @Override
  public String toString() {
    return "Component{" +
        "connectionType='" + connectionType + '\'' +
        ", connectionLabel='" + connectionLabel + '\'' +
        ", command='" + command + '\'' +
        '}';
  }
}
