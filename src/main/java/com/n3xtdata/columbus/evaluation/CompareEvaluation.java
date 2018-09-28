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

package com.n3xtdata.columbus.evaluation;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompareEvaluation implements Evaluation {

  Logger logger = LoggerFactory.getLogger(getClass());


  public CompareEvaluation() {
  }

  @Override
  public Status evaluate(ExecutionRuns runs) {


    Object firstValue = runs.get("first").get(0).get("value");
    Object secondValue = runs.get("second").get(0).get("value");

    if(firstValue.equals(secondValue)) {
      return Status.SUCCESS;
    }

    return Status.ERROR;

  }

}
