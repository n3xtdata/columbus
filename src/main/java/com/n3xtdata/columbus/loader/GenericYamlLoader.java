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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class GenericYamlLoader<T> {

  private T t;

  private List<String> fileList = new ArrayList<>();


  public GenericYamlLoader(T t) {
    this.t = t;
  }

  /**
   *
   * @param fileName
   * @return
   * @throws FileNotFoundException
   */
  public T load(String fileName) throws FileNotFoundException {

    Yaml yaml = new Yaml(new Constructor(t.getClass()));

    InputStream inputStream = new FileInputStream(fileName);

    T t = yaml.load(inputStream);

    return t;
  }

  /**
   *
   * @param path
   * @return
   */
  public List<String> getAllFiles(String path) {

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
