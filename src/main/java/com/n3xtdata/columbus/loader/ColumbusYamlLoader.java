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

package com.n3xtdata.columbus.loader;

import com.n3xtdata.columbus.config.Properties;
import com.n3xtdata.columbus.config.SpringContext;
import com.n3xtdata.columbus.connectors.jdbc.JdbcConnection;
import com.n3xtdata.columbus.connectors.ssh.SshConnection;
import com.n3xtdata.columbus.core.Check;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class ColumbusYamlLoader<T> {

  private T t;

  private List<String> fileList = new ArrayList<>();


  HashMap<String, JdbcConnection> jdbcConnections = new HashMap<>();
  HashMap<String, SshConnection> sshConnections = new HashMap<>();


  public ColumbusYamlLoader(Class<T> t) throws IllegalAccessException, InstantiationException {
    this.t = t.newInstance();
  }


  public HashMap<String, T> load() {
    GenericYamlLoader<T> loader = new GenericYamlLoader<>(t);

    ApplicationContext context = SpringContext.getAppContext();

    Properties properties = (Properties) context.getBean("properties");

    String path = properties.getHome();

    if(t instanceof Check) {
      path = path + "/checks";
    }
    else if (t instanceof JdbcConnection) {
      path = path + "/connections/jdbc";

    }
    else if(t instanceof SshConnection) {
      path = path + "/connections/ssh";

    }
    else {

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

    for (File file : listOfFiles) {
      if (file.isFile() && (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml"))) {
        this.fileList.add(path + "/" + file.getName());
      } else if (file.isDirectory()) {
        this.getAllFiles(path + "/" + file.getName());
      }
    }

    return this.fileList;

  }



}
