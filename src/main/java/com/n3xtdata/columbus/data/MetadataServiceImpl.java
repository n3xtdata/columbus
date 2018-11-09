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

import com.google.common.collect.Multimap;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.ColumbusFile;
import com.n3xtdata.columbus.core.Kind;
import com.n3xtdata.columbus.core.connection.Connection;
import com.n3xtdata.columbus.core.connection.JdbcConnection;
import com.n3xtdata.columbus.core.connection.SshConnection;
import com.n3xtdata.columbus.core.notification.Notification;
import com.n3xtdata.columbus.loader.ColumbusYamlLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MetadataServiceImpl implements MetadataService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private Multimap<Kind, ColumbusFile> allObjects;

  private final HashMap<String, Notification> notifications = new HashMap<>();
  private final HashMap<String, Check> checks = new HashMap<>();
  private final HashMap<String, JdbcConnection> jdbcConnections = new HashMap<>();
  private final HashMap<String, SshConnection> sshConnections = new HashMap<>();
  private final HashMap<String, Connection> connections = new HashMap<>();

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

  @Override
  public Set<Connection> getAllConnections() {
    return new HashSet<>(this.connections.values());
  }

  public Connection getConnectionByLabel(String label) throws Exception {
    if (this.connections.get(label) != null) {
      return this.connections.get(label);
    } else {
      throw new Exception("Connection " + label + " does not exist!");
    }
  }

  public Set<JdbcConnection> getAllJdbcConnections() {
    return new HashSet<>(this.jdbcConnections.values());
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
    ColumbusYamlLoader<ColumbusFile> objectLoader = new ColumbusYamlLoader<>(ColumbusFile.class);
    this.allObjects = objectLoader.load();
    logger.debug("MULTIMAP: " + this.allObjects.toString());

    this.loadNotifications();
    this.loadChecks();
    this.loadConnections();
  }

  private void loadNotifications() {
    Collection<ColumbusFile> columbusFiles = this.allObjects.get(Kind.notification);
    columbusFiles.forEach(this::loadNotification);
    logger.info("Number of loaded Notifications: " + this.notifications.size());
  }

  private void loadNotification(ColumbusFile columbusFile) {
    Notification notification = new Helper<>(Notification.class).getObject(columbusFile);
    this.notifications.put(notification.getLabel(), notification);
    logger.info("adding: " + notification.toString());
  }

  private void loadChecks() {
    Collection<ColumbusFile> columbusFiles = this.allObjects.get(Kind.check);
    columbusFiles.forEach(this::loadCheck);
    logger.info("Number of loaded Checks: " + this.checks.size());
  }

  private void loadCheck(ColumbusFile columbusFile) {
    Check check = new Helper<>(Check.class).getObject(columbusFile);
    this.checks.put(check.getLabel(), check);
    logger.info("adding: " + check.toString());
  }

  private void loadConnections() {
    this.loadJdbcConnections();
    this.loadSshConnections();
    this.connections.putAll(jdbcConnections);
    this.connections.putAll(sshConnections);
  }

  private void loadJdbcConnections() {
    Collection<ColumbusFile> columbusFiles = this.allObjects.get(Kind.jdbcConnection);
    columbusFiles.forEach(this::loadJdbcConnection);
    logger.info("Number of loaded JDBC Connections: " + this.jdbcConnections.size());
  }

  private void loadJdbcConnection(ColumbusFile columbusFile) {
    JdbcConnection jdbcConnection = new Helper<>(JdbcConnection.class).getObject(columbusFile);
    this.jdbcConnections.put(jdbcConnection.getLabel(), jdbcConnection);
    logger.info("adding: " + jdbcConnection.toString());
  }

  private void loadSshConnections() {
    Collection<ColumbusFile> columbusFiles = this.allObjects.get(Kind.sshConnection);
    columbusFiles.forEach(this::loadSshConnection);
    logger.info("Number of loaded SSH Connections: " + this.sshConnections.size());
  }

  private void loadSshConnection(ColumbusFile columbusFile) {
    SshConnection sshConnection = new Helper<>(SshConnection.class).getObject(columbusFile);
    this.sshConnections.put(sshConnection.getLabel(), sshConnection);
    logger.info("adding: " + sshConnection.toString());
  }

  private void add(ColumbusFile columbusFile) {
    Kind kind = columbusFile.getKind();
    switch (kind) {
      case check:
        this.loadCheck(columbusFile);
        break;
      case notification:
        this.loadNotification(columbusFile);
        break;
      case jdbcConnection:
        this.loadJdbcConnection(columbusFile);
        break;
      case sshConnection:
        this.loadSshConnection(columbusFile);
        break;
      default:
        logger.error("Could not load Object because of invalid kind: '" + kind + "'");
    }
  }
}