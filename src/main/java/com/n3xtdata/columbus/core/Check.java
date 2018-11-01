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

import com.n3xtdata.columbus.core.component.Component;
import com.n3xtdata.columbus.core.evaluation.Evaluation;
import com.n3xtdata.columbus.core.evaluation.Status;
import com.n3xtdata.columbus.core.schedule.Schedule;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import java.util.HashMap;
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

  private Evaluation evaluation;

  private List<Schedule> schedules;

  private HashMap<Status, Set<String>> notifications;

  private String path;

  @SuppressWarnings({"unused"})
  public Check() {

  }

  public Check(String label, String description, Set<Component> components,
      Evaluation evaluation, List<Schedule> schedules, HashMap<Status, Set<String>> notifications, String path) {
    this.label = label;
    this.description = description;
    this.components = components;
    this.evaluation = evaluation;
    this.schedules = schedules;
    this.notifications = notifications;
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

  public List<Schedule> getSchedules() {

    return schedules;
  }

  @SuppressWarnings({"unused"})
  public void setSchedules(List<Schedule> schedules) {

    this.schedules = schedules;
  }

  public HashMap<Status, Set<String>> getNotifications() {
    return notifications;
  }

  @SuppressWarnings({"unused"})
  public void setNotifications(HashMap<Status, Set<String>> notifications) {
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

  @SuppressWarnings({"unused"})
  public Evaluation getEvaluation() {
    return evaluation;
  }

  @SuppressWarnings({"unused"})
  public void setEvaluation(Evaluation evaluation) {

    this.evaluation = evaluation;
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
        Objects.equals(schedules, check.schedules) &&
        Objects.equals(notifications, check.notifications) &&
        Objects.equals(path, check.path);
  }

  @SuppressWarnings({"unused"})
  public void init() {
    this.evaluation.init();
    this.components.forEach(Component::initDetails);
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
    } catch (EvaluationException e) {
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

  @Override
  public String toString() {
    return "Check{" +
        "label='" + label + '\'' +
        ", description='" + description + '\'' +
        ", components=" + components +
        ", expressionlanguage=" + evaluation +
        ", schedules=" + schedules +
        ", notifications=" + notifications +
        ", path='" + path + '\'' +
        '}';
  }
}
