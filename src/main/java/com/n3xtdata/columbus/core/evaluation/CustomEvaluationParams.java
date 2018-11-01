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

package com.n3xtdata.columbus.core.evaluation;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.expressionlanguage.RuleParser;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import com.n3xtdata.columbus.utils.Params;
import java.util.List;

public class CustomEvaluationParams implements EvaluationParams {

  private List<String> rules;

  @SuppressWarnings("unchecked")
  CustomEvaluationParams(Params params) {
    this.rules = (List<String>) params.get("rules");
  }

  @SuppressWarnings("unused")
  public void setRules(List<String> rules) {
    this.rules = rules;
  }

  @Override
  public Status evaluate(ExecutionRuns runs) throws EvaluationException {

    RuleParser ruleParser = new RuleParser();
    for (String rule : rules) {
      String status = ruleParser.parseRule(rule, runs);
      if (status != null) {
        return Status.fromString(status);
      }
    }

    throw new EvaluationException("No rules applicable");
  }

  @Override
  public String toString() {
    return "CustomEvaluationParams{" +
        "rules='" + rules + '\'' +
        '}';
  }
}
