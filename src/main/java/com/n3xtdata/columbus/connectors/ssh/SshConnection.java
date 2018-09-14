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

package com.n3xtdata.columbus.connectors.ssh;

public class SshConnection {

  private String label;

  private String username;

  private String publicKey;

  private String host;

  private Integer port;

  public SshConnection() {
  }

  public SshConnection(String label, String username, String publicKey, String host, Integer port) {
    this.label = label;
    this.username = username;
    this.publicKey = publicKey;
    this.host = host;
    this.port = port;
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

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  @Override
  public String toString() {
    return "SshConnection{" +
        "label='" + label + '\'' +
        ", username='" + username + '\'' +
        ", publicKey='" + publicKey + '\'' +
        ", host='" + host + '\'' +
        ", port=" + port +
        '}';
  }
}
