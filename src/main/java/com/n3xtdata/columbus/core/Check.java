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
import com.n3xtdata.columbus.evaluation.SimpleEvaluation;
import com.n3xtdata.columbus.evaluation.Status;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Check {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private String label;

  private String description;

  private Set<Component> components;

  private EvaluationType evaluationType;

  private Evaluation evaluation;

  private List<Schedule> schedules;

  private Set<String> notifications;

  private String path;

  @SuppressWarnings({"unused"})
  public Check() {

  }

  @SuppressWarnings({"unused"})
  public Check(String label, String description, Set<Component> components, EvaluationType evaluationType,
      List<Schedule> schedules, String path) {

    this.label = label;
    this.description = description;
    this.components = components;
    this.evaluationType = evaluationType;
    this.schedules = schedules;
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

  public EvaluationType getEvaluationType() {

    return evaluationType;
  }

  @SuppressWarnings({"unused"})
  public void setEvaluationType(EvaluationType evaluationType) {
    this.evaluationType = evaluationType;
    this.setEvaluationImpl();
  }

  public List<Schedule> getSchedules() {

    return schedules;
  }

  public void setSchedules(List<Schedule> schedules) {

    this.schedules = schedules;
  }

  public Set<String> getNotifications() {
    return notifications;
  }

  public void setNotifications(Set<String> notifications) {
    this.notifications = notifications;
  }

  @SuppressWarnings({"unused"})
  public String getPath() {

    return path;
  }

  @SuppressWarnings({"unused"})
  public void setPath(String path) {

    this.path = path;
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
        ", evaluationType=" + evaluationType +
        ", evaluation=" + evaluation +
        ", schedules=" + schedules +
        ", notifications=" + notifications +
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
        evaluationType == check.evaluationType &&
        Objects.equals(evaluation, check.evaluation) &&
        Objects.equals(schedules, check.schedules) &&
        Objects.equals(notifications, check.notifications) &&
        Objects.equals(path, check.path);
  }

  @Override
  public int hashCode() {

    return Objects.hash(label, description, components, evaluationType, evaluation, schedules, notifications, path);
  }

  @SuppressWarnings({"unused"})
  public Boolean validate() {

    return this.validateComponentSize();
  }

  @SuppressWarnings({"unused"})
  public void init() {
    this.components.forEach(Component::initDetails);
  }

  private Boolean validateComponentSize() {

    return this.evaluation.validate(this.components.size());
  }


  public Status execute() {

    logger.info("Executing check " + this.label);

    ExecutionRuns runs = new ExecutionRuns();

    try {
      this.executeComponents(runs);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Could not execute components for check " + this.getLabel());
      return Status.TECHNICAL_ERROR;
    }

    Status status = this.evaluate(runs);

    logger.info("Result for check " + this.getLabel() + " is: " + status);
    return status;
  }

  private Status evaluate(ExecutionRuns runs) {

    try {
      return this.getEvaluation().evaluate(runs);
    } catch (EvaluationException | InterruptedException e) {
      logger.error("Could not evaluate Check " + this.getLabel() + ": " + e.getMessage());
      return Status.TECHNICAL_ERROR;
    }
  }

  private void executeComponents(ExecutionRuns runs) throws Exception {

    for (Component component : this.getComponents()) {
      component.execute(runs);
    }
    logger.info("Executed " + this.getComponents().size() + " components for check: " + this.getLabel());
  }


  enum EvaluationType {
    SIMPLE, COMPARE
  }
}
