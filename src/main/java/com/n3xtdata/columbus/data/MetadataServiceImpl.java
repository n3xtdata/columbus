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

package com.n3xtdata.columbus.data;

import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.connection.Connection;
import com.n3xtdata.columbus.core.connection.JdbcConnection;
import com.n3xtdata.columbus.core.connection.SshConnection;
import com.n3xtdata.columbus.core.notification.Notification;
import com.n3xtdata.columbus.loader.ColumbusYamlLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MetadataServiceImpl implements MetadataService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private HashMap<String, Check> checks;

  private HashMap<String, JdbcConnection> jdbcConnections;
  private HashMap<String, SshConnection> sshConnections;
  private HashMap<String, Notification> notifications;

  public MetadataServiceImpl() {

  }


  public Set<Check> getAllChecks() {

    return new HashSet<>(this.checks.values());
  }

  public Check getCheckByLabel(String label) throws Exception {

    if (this.checks.get(label) != null) {
      return this.checks.get(label);
    } else {
      throw new Exception("Check " + label + " does not exist!");
    }
  }

  public Set<JdbcConnection> getAllJdbcConnections() {

    return new HashSet<>(this.jdbcConnections.values());
  }

  public Connection getConnectionByLabel(String label) throws Exception {

    if (this.jdbcConnections.get(label) != null) {
      return this.jdbcConnections.get(label);
    } else {
      throw new Exception("Connection " + label + " does not exist!");
    }
  }

  public JdbcConnection getJdbcConnectionByLabel(String label) throws Exception {

    if (this.jdbcConnections.get(label) != null) {
      return this.jdbcConnections.get(label);
    } else {
      throw new Exception("JdbcConnection " + label + " does not exist!");
    }
  }

  public Set<SshConnection> getAllSshConnections() {
    return new HashSet<>(this.sshConnections.values());
  }

  public SshConnection getSshConnectionByLabel(String label) throws Exception {

    if (this.sshConnections.get(label) != null) {
      return this.sshConnections.get(label);
    } else {
      throw new Exception("SshConnection " + label + " does not exist!");
    }
  }

  public Set<Notification> getAllNotifications() {
    return new HashSet<>(this.notifications.values());
  }

  public Notification getNotificationByLabel(String label) throws Exception {

    if (this.notifications.get(label) != null) {
      return this.notifications.get(label);
    } else {
      throw new Exception("Notification " + label + " does not exist!");
    }
  }


  public void loadAll() throws Exception {
    this.loadChecks();
    this.loadJdbcConnections();
    this.loadSshConnections();
    this.loadNotifications();
  }


  private void loadChecks() throws Exception {
    ColumbusYamlLoader<Check> checkLoader = new ColumbusYamlLoader<>(Check.class);
    this.checks = checkLoader.load();
    this.checks.forEach((k, v) -> logger.info(v.toString()));
    logger.info("Number of loaded Checks: " + this.checks.size());
  }

  private void loadJdbcConnections() throws Exception {
    ColumbusYamlLoader<JdbcConnection> jdbcLoader = new ColumbusYamlLoader<>(JdbcConnection.class);
    this.jdbcConnections = jdbcLoader.load();
    this.jdbcConnections.forEach((k, v) -> logger.info(v.toString()));
    logger.info("Number of loaded JDBC Connections: " + this.jdbcConnections.size());
  }

  private void loadSshConnections() throws Exception {
    ColumbusYamlLoader<SshConnection> sshLoader = new ColumbusYamlLoader<>(SshConnection.class);
    this.sshConnections = sshLoader.load();
    this.sshConnections.forEach((k, v) -> logger.info(v.toString()));
    logger.info("Number of loaded SSH Connections: " + this.sshConnections.size());
  }

  private void loadNotifications() throws Exception {
    ColumbusYamlLoader<Notification> notificationLoader = new ColumbusYamlLoader<>(Notification.class);
    this.notifications = notificationLoader.load();
    this.notifications.forEach((k, v) -> logger.info(v.toString()));
    logger.info("Number of loaded Notifications: " + this.notifications.size());
  }

}
