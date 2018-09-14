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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class GenericYamlLoader<T> {

  private T t;


  public GenericYamlLoader(T t) {
    this.t = t;
  }

  /**
   *
   * @param fileName
   * @return
   * @throws FileNotFoundException
   */
  public HashMap<String, T> load(String fileName) throws FileNotFoundException {

    Constructor constructor = new Constructor(t.getClass());
    TypeDescription customTypeDescription = new TypeDescription(t.getClass());
    constructor.addTypeDescription(customTypeDescription);
    Yaml yaml = new Yaml(constructor);
    InputStream inputStream = new FileInputStream(fileName);
    HashMap<String, T> hashMap = new HashMap<>();
    yaml.loadAll(inputStream).forEach(element -> {
      try {
        String label = (String) element.getClass().getMethod("getLabel").invoke((T) element);

        hashMap.put(label, (T) element);

      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }

    });

    return hashMap;

  }


}
