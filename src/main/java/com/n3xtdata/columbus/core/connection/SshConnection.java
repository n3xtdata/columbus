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

package com.n3xtdata.columbus.core.connection;

import java.util.Objects;

public class SshConnection {

  private String label;

  private String username;

  private String publicKey;

  private String host;

  private Integer port;

  private String path;

  @SuppressWarnings({"unused"})
  public SshConnection() {

  }

  @SuppressWarnings({"unused"})
  public SshConnection(String label, String username, String publicKey, String host, Integer port) {

    this.label = label;
    this.username = username;
    this.publicKey = publicKey;
    this.host = host;
    this.port = port;
  }

  @SuppressWarnings({"unused"})
  public String getLabel() {

    return label;
  }

  @SuppressWarnings({"unused"})
  public void setLabel(String label) {

    this.label = label;
  }

  @SuppressWarnings({"unused"})
  public String getUsername() {

    return username;
  }

  @SuppressWarnings({"unused"})
  public void setUsername(String username) {

    this.username = username;
  }

  @SuppressWarnings({"unused"})
  public String getPublicKey() {

    return publicKey;
  }

  @SuppressWarnings({"unused"})
  public void setPublicKey(String publicKey) {

    this.publicKey = publicKey;
  }

  @SuppressWarnings({"unused"})
  public String getHost() {

    return host;
  }

  @SuppressWarnings({"unused"})
  public void setHost(String host) {

    this.host = host;
  }

  @SuppressWarnings({"unused"})
  public Integer getPort() {

    return port;
  }

  @SuppressWarnings({"unused"})
  public void setPort(Integer port) {

    this.port = port;
  }

  @SuppressWarnings({"unused"})
  public String getPath() {

    return path;
  }

  @SuppressWarnings({"unused"})
  public void setPath(String path) {

    this.path = path;
  }

  @Override
  public String toString() {

    return "SshConnection{" + "label='" + label + '\'' + ", username='" + username + '\'' + ", publicKey='" + publicKey
        + '\'' + ", host='" + host + '\'' + ", port=" + port + ", path='" + path + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SshConnection that = (SshConnection) o;
    return Objects.equals(label, that.label) && Objects.equals(username, that.username) && Objects
        .equals(publicKey, that.publicKey) && Objects.equals(host, that.host) && Objects.equals(port, that.port)
        && Objects.equals(path, that.path);
  }

  @Override
  public int hashCode() {

    return Objects.hash(label, username, publicKey, host, port, path);
  }

  public Boolean validate() {
    return true;
  }

  public void init() {

  }
}
