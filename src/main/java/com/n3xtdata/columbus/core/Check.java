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

package com.n3xtdata.columbus.core;

import java.util.Objects;
import java.util.Set;

public class Check {

  private String label;

  private String description;

  private Set<Component> components;

  @SuppressWarnings({"unused"})
  public Check() {

  }

  @SuppressWarnings({"unused"})
  public Check(String label, String description, Set<Component> components) {

    this.label = label;
    this.description = description;
    this.components = components;
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
  public String getDescription() {

    return description;
  }

  @SuppressWarnings({"unused"})
  public void setDescription(String description) {

    this.description = description;
  }

  @SuppressWarnings({"unused"})
  public Set<Component> getComponents() {

    return components;
  }

  @SuppressWarnings({"unused"})
  public void setComponents(Set<Component> components) {

    this.components = components;
  }

  @Override
  public String toString() {

    return "Check{" + "label='" + label + '\'' + ", description='" + description + '\'' + ", components=" + components
        + '}';
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Check check = (Check) o;
    return Objects.equals(label, check.label) && Objects.equals(description, check.description) && Objects
        .equals(components, check.components);
  }

  @Override
  public int hashCode() {

    return Objects.hash(label, description, components);
  }
}
