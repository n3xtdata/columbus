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

import static org.junit.Assert.assertEquals;

import com.n3xtdata.columbus.ColumbusApplicationTests;
import com.n3xtdata.columbus.core.Evaluation;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class EvaluationTests extends ColumbusApplicationTests {

  @Test
  public void shouldEvaluateSimpleQueryCheck() throws Exception {

    Evaluation evaluation = new SimpleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> records = new ArrayList<>();
    Map<String, Object> row = new HashMap<>();
    row.put("status", "SUCCESS");
    records.add(0, row);
    runs.put("first", records);

    Status status = evaluation.evaluate(runs);
    assertEquals(status, Status.SUCCESS);
  }

  @Test(expected = EvaluationException.class)
  public void shouldThrowExceptionWhenSimpleEvalReturnsMultipleRows() throws Exception {

    Evaluation evaluation = new SimpleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> records = new ArrayList<>();
    Map<String, Object> row = new HashMap<>();
    row.put("status", "SUCCESS");
    records.add(0, row);
    records.add(1, row);
    runs.put("first", records);

    evaluation.evaluate(runs);
  }

  @Test(expected = EvaluationException.class)
  public void shouldThrowExceptionWhenSimpleEvalReturnsZeroRows() throws Exception {

    Evaluation evaluation = new SimpleEvaluation();

    ExecutionRuns runs = new ExecutionRuns();
    List<Map<String, Object>> records = new ArrayList<>();
    runs.put("first", records);

    evaluation.evaluate(runs);
  }


}