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

import static org.junit.Assert.assertTrue;

import com.n3xtdata.columbus.executor.ExecutionRuns;
import com.n3xtdata.columbus.expressionlanguage.RuleParser;
import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class RuleParserTests {

  private RuleParser ruleParser;
  private ExecutionRuns runs;
  private List<Map<String, Object>> firstComponentRun;

  @Before
  public void before() {
    this.ruleParser = new RuleParser();
    this.runs = new ExecutionRuns();
    this.firstComponentRun = new ArrayList<>();
  }


  @Test
  public void testSimpleRule() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.status} >= 'B'  AND 'A' == 'A' -> SUCCESS";

    String status = this.ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }

  @Test
  public void testStringsShouldBeEqual() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "A");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.status} == 'A' -> SUCCESS";

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }

  @Test
  public void testShouldIgnoreEmptyLine() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "A");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = " \n {first.status} == 'A' -> SUCCESS";

    ruleParser.setRules(rule);
    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }

  @Test
  public void testStringsShouldBeDifferent() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "A");

    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.status} != 'B' -> WARNING";

    ruleParser.setRules(rule);
    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("WARNING"));
  }

  @Test(expected = EvaluationException.class)
  public void testNotExistingStatusShouldThrowException() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.status} >= 1 ->";
    ruleParser.setRules(rule);
    ruleParser.parseRule(rule, runs);
  }

  @Test(expected = EvaluationException.class)
  public void testInvalidComparison() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "C");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.status} >= 1 -> SUCCESS";
    ruleParser.setRules(rule);

    ruleParser.parseRule(rule, runs);
  }


  @Test(expected = EvaluationException.class)
  public void testInvalidOperatorShouldThrowException() throws EvaluationException {

    String rule = "{first.status} =! 1 -> SUCCESS";
    ruleParser.setRules(rule);

    ruleParser.parseRule(rule, runs);
  }

  @Test(expected = EvaluationException.class)
  public void testNotFoundToBeReplacedValueShouldThrowException() throws EvaluationException {

    String rule = "{first.status} == 1 -> SUCCESS";
    ruleParser.setRules(rule);

    ruleParser.parseRule(rule, runs);
  }

  @Test(expected = EvaluationException.class)
  public void testInvalidToBeReplacedValueShouldThrowException() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", "1");
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "{first.d} == 1 -> SUCCESS";
    ruleParser.setRules(rule);

    ruleParser.parseRule(rule, runs);
  }

  @Test
  public void bla() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "(({first.status} == 2) OR 2==2) AND 'a'=='b' OR 1==1 -> ERROR";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("ERROR"));

  }

  @Test
  public void bla2() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    row.put("value", 13);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "({first.status} == 1  OR  {first.value}==13  AND 'a'=='b')  OR  1==1 -> SUCCESS";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }

  @Test
  public void bla3() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "(2==2  OR  3==2) -> WARNING";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("WARNING"));
  }

  @Test
  public void bla4() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 1);
    firstComponentRun.add(row);
    runs.put("first", firstComponentRun);

    String rule = "!(1==2  OR  3==2) -> ERROR";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("ERROR"));
  }

  @Test
  public void bla5() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("status", 95);
    row.put("value", 100);
    firstComponentRun.add(row);

    runs.put("first", firstComponentRun);

    String rule = "(({first.status}/{first.value})*100) == 95 AND {first.value} == 100 -> SUCCESS";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }


  @Test
  public void bla6() throws EvaluationException {

    Map<String, Object> row = new HashMap<>();
    row.put("a", 4);
    row.put("b", 2);
    row.put("c", 3);
    firstComponentRun.add(row);

    runs.put("first", firstComponentRun);

    String rule = "{first.a} * {first.b} + {first.c} == 11 -> SUCCESS";

    ruleParser.setRules(rule);

    String status = ruleParser.parseRule(rule, runs);

    assert (status.equals("SUCCESS"));
  }

  @Test
  public void springExpressionParser() {

    org.springframework.expression.ExpressionParser parser = new SpelExpressionParser();

    Boolean bd = parser.parseExpression("(2 == 1+1 AND 2 == 2) AND !(5 == 4)").getValue(Boolean.class);

    assertTrue(bd);
  }

  @Test
  public void testSplitMethod() {
    String s = "bla.test.bla2";
    String[] splitted = s.split(Pattern.quote("."), 2);
    assert (splitted[1].equals("test.bla2"));
  }
}
