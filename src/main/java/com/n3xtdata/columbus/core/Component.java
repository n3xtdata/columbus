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

package com.n3xtdata.columbus.core;

import com.n3xtdata.columbus.core.componentDetails.ComponentDetailsFactory;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Component {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String label;

  private ComponentType componentType;

  private HashMap<String, Object> componentDetails;

  private ComponentDetails details;

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
  public ComponentType getComponentType() {
    return componentType;
  }

  @SuppressWarnings({"unused"})
  public void setComponentType(ComponentType componentType) {

    this.componentType = componentType;
  }

  @SuppressWarnings({"unused"})
  public HashMap<String, Object> getComponentDetails() {
    return componentDetails;
  }

  public void setComponentDetails(HashMap<String, Object> componentDetails) {
    this.componentDetails = componentDetails;
  }

  public void execute(ExecutionRuns runs) throws Exception {
    runs.put(this.label, this.executeConnector());
  }

  private List<Map<String, Object>> executeConnector() throws Exception {

    List<Map<String, Object>> result;

    result = this.details.execute();

    logger.debug(result.toString());

    return result;
  }

  public void initDetails() {
    this.details = ComponentDetailsFactory.build(this.componentType, this.componentDetails);
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
        componentType == component.componentType &&
        Objects.equals(componentDetails, component.componentDetails) &&
        Objects.equals(details, component.details);
  }

  @Override
  public int hashCode() {

    return Objects.hash(logger, label, componentType, componentDetails, details);
  }

  @Override
  public String toString() {
    return "Component{" +
        "label='" + label + '\'' +
        ", componentType=" + componentType +
        ", componentDetails=" + componentDetails +
        ", details=" + details +
        '}';
  }

  public enum ComponentType {
    JDBC
  }

}
