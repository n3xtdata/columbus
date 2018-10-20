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

package com.n3xtdata.columbus.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecutionRuns {

  private final Map<String, List<Map<String, Object>>> runs;

  public ExecutionRuns() {
    this.runs = new HashMap<>();
  }

  public void put(String key, List<Map<String, Object>> value) {
    this.runs.put(key, value);
  }

  public List<Map<String, Object>> get(String key) {
    return this.runs.get(key);
  }

  public Object getValue(String componentName, String field) throws NullPointerException {
    Object o = runs.get(componentName).get(0).get(field);
    if (o == null) {
      throw new NullPointerException();
    }
    return o;
  }

}
