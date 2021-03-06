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
import java.util.Set;

public interface MetadataService {

  Set<Check> getAllChecks();

  Check getCheckByLabel(String label) throws Exception;

  Set<Connection> getAllConnections();

  Connection getConnectionByLabel(String label) throws Exception;

  Set<JdbcConnection> getAllJdbcConnections();

  JdbcConnection getJdbcConnectionByLabel(String label) throws Exception;

  Set<SshConnection> getAllSshConnections();

  SshConnection getSshConnectionByLabel(String label) throws Exception;

  Set<Notification> getAllNotifications();

  Notification getNotificationByLabel(String label) throws Exception;

  void loadAll() throws Exception;

}
