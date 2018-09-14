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


public class JdbcConnection {

  private String label;

  private String username;

  private String password;

  private String url;

  private String driverClass;

  public JdbcConnection() {
  }

  public JdbcConnection(String label, String username, String password, String url, String driverClass) {
    this.label = label;
    this.username = username;
    this.password = password;
    this.url = url;
    this.driverClass = driverClass;
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

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  @Override
  public String toString() {
    return "JdbcConnection{" +
        "label='" + label + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", url='" + url + '\'' +
        ", driverClass='" + driverClass + '\'' +
        '}';
  }
}
