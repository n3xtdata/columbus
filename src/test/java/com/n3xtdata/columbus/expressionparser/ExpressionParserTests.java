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

package com.n3xtdata.columbus.expressionparser;

import com.n3xtdata.columbus.ColumbusApplicationTests;
import com.n3xtdata.columbus.evaluation.RuleEvaluation;
import com.n3xtdata.columbus.evaluation.Status;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Test;

public class ExpressionParserTests {

  @Test
  public void testSimpleRule() throws EvaluationException {

    RuleEvaluation ruleEvaluation = new RuleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> firstComponentRun = new ArrayList<>();

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} >= 'B' -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);
    Status status = ruleEvaluation.evaluate(runs);

    assert(status.equals(Status.SUCCESS));
  }

  @Test
  public void testEqualsStrings() throws EvaluationException {

    RuleEvaluation ruleEvaluation = new RuleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> firstComponentRun = new ArrayList<>();

    Map<String, Object> row = new HashMap<>();
    row.put("status", "1");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} == 1 -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);
    Status status = ruleEvaluation.evaluate(runs);

    assert(status.equals(Status.SUCCESS));
  }

  @Test(expected = EvaluationException.class)
  public void testInvalidComparison() throws EvaluationException {

    RuleEvaluation ruleEvaluation = new RuleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> firstComponentRun = new ArrayList<>();

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} >= 1 -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);
    Status status = ruleEvaluation.evaluate(runs);

    assert(status.equals(Status.SUCCESS));
  }
}
