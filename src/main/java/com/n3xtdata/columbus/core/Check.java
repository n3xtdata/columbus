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

import com.n3xtdata.columbus.evaluation.CompareEvaluation;
import com.n3xtdata.columbus.evaluation.Evaluation;
import com.n3xtdata.columbus.evaluation.SimpleEvaluation;
import com.n3xtdata.columbus.evaluation.Status;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.sun.istack.internal.NotNull;
import java.util.Objects;
import java.util.Set;

public class Check {

  private String label;

  private String description;

  private Set<Component> components;

  private EvaluationType evaluationType;

  @NotNull
  private Evaluation evaluation;

  private String path;

  @SuppressWarnings({"unused"})
  public Check() {

  }

  @SuppressWarnings({"unused"})
  public Check(String label, String description, Set<Component> components,
      EvaluationType evaluationType, String path) {
    this.label = label;
    this.description = description;
    this.components = components;
    this.evaluationType = evaluationType;
    this.path = path;
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

  @SuppressWarnings({"unused"})
  public String getPath() {

    return path;
  }

  @SuppressWarnings({"unused"})
  public void setPath(String path) {

    this.path = path;
  }

  @SuppressWarnings({"unused"})
  public void setEvaluationType(EvaluationType evaluationType) {
    this.evaluationType = evaluationType;
    this.setEvaluationImpl();
  }

  private void setEvaluationImpl() {
    if (this.evaluationType == EvaluationType.SIMPLE) {
      this.evaluation = new SimpleEvaluation();
    } else if (this.evaluationType == EvaluationType.COMPARE) {
      this.evaluation = new CompareEvaluation();
    }
  }

  @SuppressWarnings({"unused"})
  public Evaluation getEvaluation() {
    return evaluation;
  }

  @SuppressWarnings({"unused"})
  public void setEvaluation(Evaluation evaluation) {

    this.evaluation = evaluation;
  }

  @Override
  public String toString() {
    return "Check{" +
        "label='" + label + '\'' +
        ", description='" + description + '\'' +
        ", components=" + components +
        ", evaluation=" + evaluation +
        ", path='" + path + '\'' +
        '}';
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
    return Objects.equals(label, check.label) &&
        Objects.equals(description, check.description) &&
        Objects.equals(components, check.components) &&
        Objects.equals(evaluation, check.evaluation) &&
        Objects.equals(path, check.path);
  }

  @Override
  public int hashCode() {

    return Objects.hash(label, description, components, evaluation, path);
  }

  @SuppressWarnings({"unused"})
  public Boolean validate() {
    if (this.evaluation instanceof SimpleEvaluation) {
      return this.validateSimple();
    } else if (this.evaluation instanceof CompareEvaluation) {
      return this.validateCompare();
    }
    return false;
  }

  public Status evaluate(ExecutionRuns runs) {
    return this.getEvaluation().evaluate(runs);
  }


  private Boolean validateSimple() {
    return this.components.size() == 1;
  }

  private Boolean validateCompare() {
    return this.components.size() == 2;
  }

  enum EvaluationType {
    SIMPLE, COMPARE
  }
}
