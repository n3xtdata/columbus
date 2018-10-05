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

package com.n3xtdata.columbus.core.componentDetails;

import com.n3xtdata.columbus.core.Component.ComponentType;
import com.n3xtdata.columbus.core.ComponentDetails;
import java.util.HashMap;

public class ComponentDetailsFactory {

  public static ComponentDetails build(ComponentType componentType, HashMap<String, Object> map) {
    if (componentType == ComponentType.JDBC) {
      return new JdbcComponentDetails(map);
    } else {
      return null;
    }
  }

}
