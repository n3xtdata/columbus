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

import com.n3xtdata.columbus.connectors.jdbc.JdbcConnectorService;
import com.n3xtdata.columbus.core.Check;
import com.n3xtdata.columbus.core.Component;
import com.n3xtdata.columbus.data.MetadataService;
import com.n3xtdata.columbus.evaluation.Status;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.notifications.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final MetadataService metadataService;
  private final JdbcConnectorService jdbcConnectorService;
  private final NotificationService notificationService;

  public ExecutionServiceImpl(MetadataService metadataService, JdbcConnectorService jdbcConnectorService,
      NotificationService notificationService) {

    this.metadataService = metadataService;
    this.jdbcConnectorService = jdbcConnectorService;
    this.notificationService = notificationService;
  }

  @Override
  public Status execute(String checkLabel) throws Exception {

    logger.info("Executing check " + checkLabel);

    ExecutionRuns runs = new ExecutionRuns();

    Check check = this.metadataService.getCheckByLabel(checkLabel);

    try {
      this.executeComponents(runs, check);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Could not execute components for check " + check.getLabel());
      return Status.TECHNICAL_ERROR;
    }

    Status status = this.evaluate(runs, check);

    logger.info("Result for check " + check.getLabel() + " is: " + status);

    if (check.getNotifications().size() > 0) {
      if (status.equals(Status.ERROR) || status.equals(Status.WARNING)) {
        logger.info("Sending mail for check " + check.getLabel() + " to " + check.getNotifications().toString());
        this.notificationService.sendNotification(check.getNotifications());
      }
    }
    return status;
  }

  private Status evaluate(ExecutionRuns runs, Check check) {
    try {
      return check.getEvaluation().evaluate(runs);
    } catch (EvaluationException e) {
      logger.error("Could not evaluate Check " + check.getLabel() + ": " + e.getMessage());
      return Status.TECHNICAL_ERROR;
    }
  }

  private void executeComponents(ExecutionRuns runs, Check check) throws Exception {

    for (Component component : check.getComponents()) {
      component.execute(runs, metadataService, jdbcConnectorService);
    }
    logger.info("Executed " + check.getComponents().size() + " components for check: " + check.getLabel());
  }
}