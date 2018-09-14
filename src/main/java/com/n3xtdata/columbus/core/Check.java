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

package com.n3xtdata.columbus.core;

import java.util.Set;

public class Check {

  private String label;

  private String description;

  private Set<Component> components;


  public Check() {
  }

  public Check(String label, String description,
      Set<Component> components) {
    this.label = label;
    this.description = description;
    this.components = components;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Component> getComponents() {
    return components;
  }

  public void setComponents(Set<Component> components) {
    this.components = components;
  }

  @Override
  public String toString() {
    return "Check{" +
        "label='" + label + '\'' +
        ", description='" + description + '\'' +
        ", components=" + components +
        '}';
  }
}
