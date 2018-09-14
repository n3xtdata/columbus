package com.n3xtdata.columbus.connectors.jdbc;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;

/**
 * This class implements a driver factory that returns a jdb driver instance loaded from the byte contents of a jar
 *
 * This enables loading of jdbc driver at runtime, the driver does not need to be included in the classpath, as
 * a proxy class is instatiated which delegates to the actual driver instance.
 *
 * @see DriverProxy
 *
 */
@Component
public class DriverFactory {

    public Driver createDriverFromJar(String connectorClass, byte[] jarContents) throws DriverLoadException {
        Class<?> klass;

        try {
            klass = getClass().getClassLoader().loadClass(connectorClass);

        } catch (ClassNotFoundException e) {
            klass = loadClassFromJar(connectorClass, jarContents);
        }
        return getDriverFromClass(connectorClass, klass);
    }

    private Driver getDriverFromClass(String connectorClass, Class<?> klass) throws DriverLoadException {
        try {
            Driver d = (Driver) klass.newInstance();
            return new DriverProxy(d);
        } catch (Exception e) {
            throw new DriverLoadException("unable to instantiate " + connectorClass, e);
        }
    }

    private Class<?> loadClassFromJar(String connectorClass, byte[] jarContents) throws DriverLoadException {

        try {
            File tempFile = File.createTempFile("jdbc", "driver");
            try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                outputStream.write(jarContents);
                outputStream.close();
                URL url = tempFile.toURI().toURL();
                return loadClassFromJarURL(connectorClass, url);
            }
        } catch (Exception e) {
            throw new DriverLoadException("could not load class " + connectorClass + " from jar given as bytes array", e);
        }
    }

    private Class<?> loadClassFromJarURL(String connectorClass, URL jarURL) throws ClassNotFoundException{
        URLClassLoader cl = new URLClassLoader(new URL[]{ jarURL });
        return cl.loadClass(connectorClass);
    }
}