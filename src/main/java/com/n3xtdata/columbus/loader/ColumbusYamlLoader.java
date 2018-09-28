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

package com.n3xtdata.columbus.loader;

import com.n3xtdata.columbus.config.Properties;
import com.n3xtdata.columbus.config.SpringContext;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.JdbcConnection;
import com.n3xtdata.columbus.core.SshConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class ColumbusYamlLoader<T> {

  private static final ApplicationContext context = SpringContext.getAppContext();
  private static final Properties properties = (Properties) context.getBean("properties");
  @SuppressWarnings("CanBeFinal")
  private static String COLUMBUS_HOME = properties.getHome();
  private final List<String> fileList = new ArrayList<>();
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private T t;

  public ColumbusYamlLoader(Class<T> t) throws IllegalAccessException, InstantiationException {

    this.t = t.newInstance();
  }


  public HashMap<String, T> load() throws Exception {

    GenericYamlLoader<T> loader = new GenericYamlLoader<>(t);

    String path = COLUMBUS_HOME;

    if (t instanceof Check) {
      path = path + "/checks";
    } else if (t instanceof JdbcConnection) {
      path = path + "/connections/jdbc";

    } else if (t instanceof SshConnection) {
      path = path + "/connections/ssh";

    } else {
      throw new Exception("The given type is not supported");
    }

    List<String> files = this.getAllFiles(path);

    HashMap<String, T> hashMap = new HashMap<>();

    files.forEach(file -> {

      try {
        hashMap.putAll(loader.load(file));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

    });

    return hashMap;
  }


  private List<String> getAllFiles(String path) {

    File folder = new File(path);

    File[] listOfFiles = folder.listFiles();

    try {
      Objects.requireNonNull(listOfFiles);
      for (File file : listOfFiles) {
        if (file.isFile() && (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"))) {
          this.fileList.add(path + "/" + file.getName());
        } else if (file.isDirectory()) {
          this.getAllFiles(path + "/" + file.getName());
        }
      }
    } catch (NullPointerException e) {
      logger.error("Folder " + folder.getPath() + " does not exist!");
      System.exit(0);
    }

    return this.fileList;

  }


}
