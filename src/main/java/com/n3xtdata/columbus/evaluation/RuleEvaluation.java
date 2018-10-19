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

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("MagicConstant")
public class RuleEvaluation implements Evaluation {

  private final String[] operators = {"==", "!=", "<=", ">=", "<", ">"};
  private Logger logger = LoggerFactory.getLogger(getClass());
  private String allRules;
  private String boolRules;

  @Override
  public Status evaluate(ExecutionRuns runs) throws RuntimeException, InterruptedException, EvaluationException {

    String status = this.parseRules(runs);
    return Status.contains(status);
  }


  private String parseRules(ExecutionRuns runs) throws RuntimeException, InterruptedException, EvaluationException {

    String[] rules = this.allRules.split(Pattern.quote("\n"));

    String result;

    for (String rule : rules) {
      if (isEmptyLine(rule)) {
        continue;
      }


      result = this.parseRule(rule, runs);

      if(result != "NO_RESULT") {
        return result;
      }

    }

    throw new EvaluationException("No rules available");
  }

  private Boolean isEmptyLine(String line) {
    return removeAllWhitespaces(line).isEmpty();
  }

  private String removeAllWhitespaces(String str) {
    return str.replaceAll(" ", "");
  }

  private String parseRule(String rule, ExecutionRuns runs) throws RuntimeException, InterruptedException, EvaluationException {
    logger.debug("Parsing rule: " + rule);
    String[] leftRightAndAction = splitByStr(rule, "->");

    String leftRight = leftRightAndAction[0];
    String action = leftRightAndAction[1];
    System.out.println("ACTION = " + action);

    this.boolRules = leftRight;

    List<String> allExpressions = new ArrayList();

    String[] orExpressions = leftRight.split(Pattern.quote("|"));

    for(String orExpression : orExpressions) {
      String[] andExpressions = orExpression.split(Pattern.quote("&"));
      for(String andExpression : andExpressions) {

        String[] all = andExpression.split(Pattern.quote("! "));

        for(String exp : all) {

          exp = exp.replaceAll(Pattern.quote("("), "");
          exp = exp.replaceAll(Pattern.quote(")"), "");

          allExpressions.add(exp);
        }
      }
    }

    this.boolRules = this.boolRules.replaceAll(Pattern.quote(" "), "");

    for(String exp : allExpressions) {
      for (String op : operators) {
        if (exp.contains(op)) {
          System.out.println("Parsing expression " + exp);
          String[] leftAndRight = splitByStr(exp, op);
          Expression ex = new Expression(leftAndRight[0], op, leftAndRight[1], leftRightAndAction[1]);

          this.replaceValues(ex, runs);

          Boolean eval = ex.evaluate();
          System.out.println("eval = " +  eval.toString());
          // ({first.status} == 1 | 2==2 & a == b) | 1==1) -> SUCCESS
          System.out.println("Bool String before = " + this.boolRules);

          String originalExpression = ex.getOriginExpressionString();

          originalExpression = originalExpression.replaceAll(Pattern.quote(" "), "");

          Boolean contains = this.boolRules.contains(originalExpression);


          this.boolRules = this.boolRules.replace(originalExpression, eval.toString());
          System.out.println("Bool String after = " + this.boolRules);


        }
      }
/*      throw new EvaluationException("Operator is not supported! Please use one of those: " + Arrays
              .toString(this.operators));*/

    }

    BooleanEvaluator booleanEvaluator = new BooleanEvaluator();

/*
    this.boolRules = this.boolRules.replaceAll(Pattern.quote("OR"), " OR ");
    this.boolRules = this.boolRules.replaceAll(Pattern.quote("AND"), " AND ");
    this.boolRules = this.boolRules.replaceAll(Pattern.quote("NOT"), " NOT ");

*/


    System.out.println("Final Bool String = " +this.boolRules);

    try {

      Boolean result = booleanEvaluator.evaluate(this.boolRules);

      if(result) {
        System.out.println("Result for rule " + rule + " = " + action);
        return action.replaceAll(Pattern.quote(" "), "");
      }

    }
    catch (java.lang.RuntimeException e) {
      throw new EvaluationException("Expression " + this.boolRules + " malformed");
    }

    return "NO_RESULT";

  }

  private String[] splitByStr(String part, String str) {
    return part.split(Pattern.quote(str));
  }

  private void replaceValues(Expression expression, ExecutionRuns runs) throws EvaluationException {
    logger.debug("Expression before replacement: " + expression.toString());
    String left = expression.getLeft();
    if (left.contains("{")) {
      Object o = this.getObject(left, runs);
      expression.setLeftObject(getDataType(o));
    } else {
      left = left.replaceAll("^\\s+", "");
      left = left.replaceAll("\\s+$", "");
      left = left.replace("'", "");

      Object o = getDataType(left);
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
      expression.setRightObject(o);
    }
    logger.debug("Expression after replacement: " + expression.toString());
  }

  private Object getObject(String toBeReplaced, ExecutionRuns runs) throws EvaluationException {
    toBeReplaced = removeAllWhitespaces(toBeReplaced);
    String[] componentAndField = splitByStr(removeBrackets(toBeReplaced), ".");
    try {
      String component = componentAndField[0];
      String field = componentAndField[1];
      return getValue(component, field, runs);
    } catch (NullPointerException e) {
      throw new EvaluationException("Could not find given value: " + toBeReplaced);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new EvaluationException(
          "Value to replace is: " + toBeReplaced + " but should be like {componentLabel.field}");
    }
  }

  private String removeBrackets(String str) {
    return str.replace("{", "").replace("}", "");
  }

  private Object getDataType(Object o) throws EvaluationException {
    try {
      return new BigDecimal(o.toString());
    } catch (NumberFormatException e) {
      return o;
    } catch (NullPointerException e) {
      throw new EvaluationException("Could not convert Object because of Nullpointer");
    }
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
