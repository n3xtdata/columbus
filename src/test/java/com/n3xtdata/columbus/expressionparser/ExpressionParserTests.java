/*
 * Copyright 2018 https://github.com/n3xtdata
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may ! use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law  OR  agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES  OR  CONDITIONS OF ANY KIND, either express  OR  implied. See the License for the
 * specific language governing permissions ? limitations under the License.
 */

package com.n3xtdata.columbus.expressionparser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.n3xtdata.columbus.evaluation.RuleEvaluation;
import com.n3xtdata.columbus.evaluation.Status;
import com.n3xtdata.columbus.evaluation.booleanevaluator.BooleanEvaluator;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class ExpressionParserTests {

  private RuleEvaluation ruleEvaluation;
  private ExecutionRuns runs;
  private List<Map<String, Object>> firstComponentRun;

  @Before
  public void before() {
    this.ruleEvaluation = new RuleEvaluation();
    this.runs = new ExecutionRuns();
    this.firstComponentRun = new ArrayList<>();
  }


  @Test
  public void testSimpleRule() throws EvaluationException, InterruptedException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} >= 'B'  AND 'A' == 'A' -> SUCCESS";

    this.ruleEvaluation.setAllRules(allRules);
    Status status = this.ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.SUCCESS));
  }

  @Test
  public void testStringsShoudlBeEqual() throws EvaluationException, InterruptedException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "A");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = " \n {first.status} == 'A' -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);
    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.SUCCESS));
  }

  @Test
  public void testStringsShoudlBeDifferent() throws EvaluationException, InterruptedException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "A");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} == 'B' -> SUCCESS \n {first.status} != 'C' -> WARNING";

    ruleEvaluation.setAllRules(allRules);
    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.WARNING));
  }


  @Test(expected = EvaluationException.class)
  public void testInvalidComparison() throws EvaluationException, InterruptedException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.status} >= 1 -> SUCCESS";
    ruleEvaluation.setAllRules(allRules);

    ruleEvaluation.evaluate(runs);
  }


  @Test(expected = EvaluationException.class)
  public void testInvalidOperatorShouldThrowException() throws EvaluationException, InterruptedException {

    String allRules = "{first.status} =! 1 -> SUCCESS";
    ruleEvaluation.setAllRules(allRules);

    ruleEvaluation.evaluate(runs);
  }

  @Test(expected = EvaluationException.class)
  public void testNotFoundToBeReplacedValueShouldThrowException() throws EvaluationException, InterruptedException {

    String allRules = "{first.status} == 1 -> SUCCESS";
    ruleEvaluation.setAllRules(allRules);

    ruleEvaluation.evaluate(runs);
  }

  @Test(expected = EvaluationException.class)
  public void testInvalidToBeReplacedValueShouldThrowException() throws EvaluationException, InterruptedException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "1");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "{first.d} == 1 -> SUCCESS";
    ruleEvaluation.setAllRules(allRules);

    ruleEvaluation.evaluate(runs);
  }

  @Test
  public void bla() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "(({first.status} == 2) OR 2==2) AND 'a'=='b' OR 1==1 -> ERROR \n {first.status} == 1 OR 2==2  AND 'a'=='a' -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.ERROR));

  }


  @Test
  public void bla2() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    row.put("value", 13);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "({first.status} == 2  OR  {first.status} == 3)  OR  {first.value}==13  AND 'a'=='b'  OR  (2==3  AND 1==1) -> ERROR \n ({first.status} == 1  OR  {first.value}==13  AND 'a'=='b')  OR  1==1 -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.SUCCESS));

  }

  @Test
  public void bla3() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "(1==2  OR  3==2) -> ERROR \n (2==2  OR  3==2) -> WARNING";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.WARNING));
  }

  @Test
  public void bla4() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String allRules = "!(1==2  OR  3==2) -> ERROR";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.ERROR));
  }

  @Test
  public void bla5() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 95);
    row.put("value", 100);
    firstComponentRun.add(row);

    runs.put("first", firstComponentRun);

    String allRules = "(({first.status}/{first.value})*100) == 95  AND {first.value} == 100 -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.SUCCESS));
  }


  @Test
  public void bla6() throws InterruptedException, EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("a", 4);
    row.put("b", 2);
    row.put("c", 3);
    firstComponentRun.add(row);

    runs.put("first", firstComponentRun);

    String allRules = "{first.a} * {first.b} + {first.c} == 11 -> SUCCESS";

    ruleEvaluation.setAllRules(allRules);

    Status status = ruleEvaluation.evaluate(runs);

    assert (status.equals(Status.SUCCESS));
  }

  @Test
  public void springExpressionParser() {

    ExpressionParser parser = new SpelExpressionParser();

    Boolean bd = parser.parseExpression("(2 == 1+1 AND 2 == 2) AND !(5 == 4)").getValue(Boolean.class);

    assertTrue(bd);

  }

}
