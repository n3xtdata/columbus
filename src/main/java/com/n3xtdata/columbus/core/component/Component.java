/*
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

package com.n3xtdata.columbus.core.component;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.utils.Params;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String label;

  private ComponentType type;

  private Params params;

  private ComponentParams componentParams;

  @SuppressWarnings({"unused"})
  public Component() {

  }

  @SuppressWarnings({"unused"})
  public String getLabel() {

    return label;
  }

  @SuppressWarnings({"unused"})
  public void setLabel(String label) {
    this.label = label;
  }

  @SuppressWarnings({"unused"})
  public ComponentType getType() {
    return type;
  }

  @SuppressWarnings({"unused"})
  public void setType(ComponentType type) {

    this.type = type;
  }

  @SuppressWarnings({"unused"})
  public Params getParams() {
    return params;
  }

  public void setParams(Params params) {
    this.params = params;
  }

  public void execute(ExecutionRuns runs) throws Exception {
    runs.put(this.label, this.executeConnector());
  }

  private List<Map<String, Object>> executeConnector() throws Exception {

    List<Map<String, Object>> result;

    result = this.componentParams.execute();

    logger.debug(result.toString());

    return result;
  }

  public void initDetails() {
    this.componentParams = ComponentParamsFactory.build(this.type, this.params);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Component component = (Component) o;
    return Objects.equals(logger, component.logger) &&
        Objects.equals(label, component.label) &&
        type == component.type &&
        Objects.equals(params, component.params) &&
        Objects.equals(componentParams, component.componentParams);
  }

  @Override
  public int hashCode() {
    return Objects.hash(logger, label, type, params, componentParams);
  }

  @Override
  public String toString() {
    return "Component{" +
        "label='" + label + '\'' +
        ", type=" + type +
        ", params=" + params +
        ", componentParams=" + componentParams +
        '}';
  }


}
