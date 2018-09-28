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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecutionServiceImpl implements ExecutionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final MetadataService metadataService;
  private final JdbcConnectorService jdbcConnectorService;

  public ExecutionServiceImpl(MetadataService metadataService,
      JdbcConnectorService jdbcConnectorService) {
    this.metadataService = metadataService;
    this.jdbcConnectorService = jdbcConnectorService;
  }

  @Override
  public Status execute(String checkLabel) throws Exception {

    logger.info("Executing check " + checkLabel);

    ExecutionRuns executionResults = new ExecutionRuns();

    Check check = this.metadataService.getCheckByLabel(checkLabel);

    try {
      this.executeComponents(executionResults, check);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Could not execute components for check " + check.getLabel());
      return Status.TECHNICAL_ERROR;
    }

    Status status = check.getEvaluation().evaluate(executionResults);
    logger.info("Result for check " + check.getLabel() + " is: " + status);
    return status;
  }

  private void executeComponents(ExecutionRuns results, Check check) throws Exception {
    for (Component component : check.getComponents()) {
      component.execute(results, metadataService, jdbcConnectorService);
    }
    logger.info("Executed " + check.getComponents().size() + " components for check: " + check.getLabel());
  }
}