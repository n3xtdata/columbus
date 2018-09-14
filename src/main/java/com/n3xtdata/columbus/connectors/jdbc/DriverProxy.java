package com.n3xtdata.columbus.connectors.jdbc;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A simple proxy class that wraps a jdbc driver object.
 *
 * As the proxy class is in the class path at compile time, one can dynamically load driver classes at runtime, as
 * the DriverManager refuses to use a driver not loaded by the system classloader.
 *
 * @see DriverManager
 */
public class DriverProxy implements Driver{

    private Driver driver;

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

    public Driver getWrappedDriver() {
        return driver;
    }
}