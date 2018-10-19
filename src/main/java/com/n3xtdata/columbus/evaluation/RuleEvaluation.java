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

import com.n3xtdata.columbus.core.Evaluation;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class RuleEvaluation implements Evaluation {

  private String allRules;

  private final String[] operators = {"==", "!=", "<=", ">=", "<", ">"};

  @Override
  public Status evaluate(ExecutionRuns runs) throws EvaluationException {

    String status = this.parseRules(runs);
    return Status.contains(status);
  }


  private String parseRules(ExecutionRuns runs) throws EvaluationException {

    String[] rules = this.allRules.split(Pattern.quote("\n"));
    String returnValue = null;

    for (String rule : rules) {

      Expression expression;
      try {
        expression = this.parseRule(rule);
        System.out.println(expression.toString());
        this.replaceValues(expression, runs);
        System.out.println("NEW: " + expression.toString());
        if (expression.evaluate()) {
          returnValue = expression.getAction();
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new EvaluationException();
      }

    }

    return returnValue;
  }

  private Expression parseRule(String rule) throws Exception {

    String[] leftRightAndAction = splitByOperator(rule, "->");
    String leftRight = leftRightAndAction[0];

    for (String op : operators) {
      if (leftRight.contains(op)) {
        String[] leftAndRight = splitByOperator(leftRight, op);
        return new Expression(leftAndRight[0], op, leftAndRight[1], leftRightAndAction[1]);
      }
    }
    throw new Exception();
  }

  private void replaceValues(Expression expression, ExecutionRuns runs) {
    String left = expression.getLeft();
    if (left.contains("{")) {
      Object o = this.getObject(left, runs);
      expression.setLeftObject(getDataType(o));
    } else {
      left = left.replaceAll("^\\s+", "");
      left = left.replaceAll("\\s+$", "");
      left = left.replace("'", "");

      Object o = getDataType(left);
      System.out.println(o);
      System.out.println(o.getClass());
      expression.setLeftObject(o);
    }

    String right = expression.getRight();
    if (right.contains("{")) {
      Object o = this.getObject(right, runs);
      expression.setRightObject(getDataType(o));
    } else {
      right = right.replaceAll("^\\s+", "");
      right = right.replaceAll("\\s+$", "");
      right = right.replace("'", "");

      Object o = getDataType(right);
      System.out.println(o);
      System.out.println(o.getClass());
      expression.setRightObject(o);


    }

    System.out.println("LeftO: " + expression.getLeftObject().getClass());
    System.out.println("RightO: " + expression.getRightObject().getClass());
  }

  private Object getObject(String toBeReplaced, ExecutionRuns runs) {
    String[] componentAndField = toBeReplaced.replaceAll(" ", "")
        .replace("{", "")
        .replace("}", "")
        .split(Pattern.quote("."));
    String component = componentAndField[0];
    String field = componentAndField[1];
    return runs.get(component).get(0).get(field);
  }

  private Object getDataType(Object o) {

    try {
      return new BigDecimal(o.toString());
    } catch (NumberFormatException e) {
      return o;
    }
  }


  private String[] splitByOperator(String part, String operator) {
    return part.split(Pattern.quote(operator));
  }


  private Object getValue(String componentName, String field, ExecutionRuns runs) {
    return runs.get(componentName).get(0).get(field);
  }

  public String getAllRules() {
    return allRules;
  }

  public void setAllRules(String allRules) {
    this.allRules = allRules;
  }

  @Override
  public Boolean validate(Integer componentSize) {
    return true;
  }

}
