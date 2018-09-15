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

package com.n3xtdata.columbus.connectors.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A simple proxy class that wraps a jdbc driver object.
 *
 * As the proxy class is in the classpath at compile time, one can dynamically load driver classes at runtime, as the
 * DriverManager refuses to use a driver not loaded by the system classloader.
 *
 * @see java.sql.DriverManager
 */
class DriverProxy implements Driver {

  private final Driver driver;

  public DriverProxy(Driver d) {

    this.driver = d;
  }

  @Override
  public boolean acceptsURL(String u) throws SQLException {

    return this.driver.acceptsURL(u);
  }

  @Override
  public java.sql.Connection connect(String u, Properties p) throws SQLException {

    Connection connect = this.driver.connect(u, p);
    if (connect == null) {
      throw new SQLException("could not connect with " + u);
    }
    return connect;
  }

  @Override
  public int getMajorVersion() {

    return this.driver.getMajorVersion();
  }

  @Override
  public int getMinorVersion() {

    return this.driver.getMinorVersion();
  }

  @Override
  public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {

    return this.driver.getPropertyInfo(u, p);
  }

  @Override
  public boolean jdbcCompliant() {

    return this.driver.jdbcCompliant();
  }

  @Override
  public Logger getParentLogger() throws SQLFeatureNotSupportedException {

    return driver.getParentLogger();
  }

}