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

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import org.springframework.stereotype.Component;

/**
 * This class implements a driver factory that returns a jdb driver instance loaded from the byte contents of a jar.
 * This enables loading of a jdbc driver at runtime. The driver does not need to be included in the classpath, as a
 * proxy class is instantiated which delegates to the actual driver instance.
 *
 * @see DriverProxy
 */
@Component
class DriverFactory {

  public Driver createDriverFromJar(String connectorClass, byte[] jarContents) throws DriverLoadException {

    Class<?> clazz;

    try {
      clazz = getClass().getClassLoader().loadClass(connectorClass);

    } catch (ClassNotFoundException e) {
      clazz = loadClassFromJar(connectorClass, jarContents);
    }
    return getDriverFromClass(connectorClass, clazz);
  }

  private Driver getDriverFromClass(String connectorClass, Class<?> specificClass) throws DriverLoadException {

    try {
      Driver d = (Driver) specificClass.newInstance();
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

  private Class<?> loadClassFromJarURL(String connectorClass, URL jarURL) throws ClassNotFoundException {

    URLClassLoader cl = new URLClassLoader(new URL[]{jarURL});
    return cl.loadClass(connectorClass);
  }
}