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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n3xtdata.columbus.core.ColumbusFile;
import java.lang.reflect.InvocationTargetException;

class Helper<T> {

  private final Class<T> t;

  Helper(Class<T> t) {
    this.t = t;
  }

  T getObject(ColumbusFile o) {
    ObjectMapper mapper = new ObjectMapper();
    T element = mapper.convertValue(o.getMetadata(), t);
    try {
      element.getClass().getMethod("init").invoke(element);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return element;
  }
}