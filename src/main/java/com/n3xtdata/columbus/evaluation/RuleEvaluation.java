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
import com.n3xtdata.columbus.evaluation.booleanevaluator.BooleanEvaluator;
import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

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

    String[] rules = this.splitByStr(this.allRules, "\n");

    String result;

    for (String rule : rules) {
      if (isEmptyLine(rule)) {
        continue;
      }

      result = this.parseRule(rule, runs);

      if (result != "NO_RESULT") {
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

  private String parseRule(String rule, ExecutionRuns runs) throws RuntimeException, EvaluationException {
    logger.debug("Parsing rule: " + rule);
    String[] leftRightAndAction = this.splitByStr(rule, "->");

    String leftRight = leftRightAndAction[0];
    String action = leftRightAndAction[1];

    ExpressionParser parser = new SpelExpressionParser();


    leftRight = this.replaceValues(leftRight, runs);


    Boolean result = parser.parseExpression(leftRight).getValue(Boolean.class);


    if(result) {
      return action.replaceAll(Pattern.quote(" "), "");
    }
    else {
      return "NO_RESULT";
    }

    /*this.boolRules = leftRight;

    List<String> allExpressions = this.extractExpressions(leftRight);

    this.boolRules = this.boolRules.replaceAll(Pattern.quote(" "), "");

    for (String exp : allExpressions) {
      for (String op : operators) {
        if (exp.contains(op)) {
          logger.debug("Parsing expression " + exp);
          String[] leftAndRight = this.splitByStr(exp, op);
          Expression convertedExpression = new Expression(leftAndRight[0], op, leftAndRight[1], leftRightAndAction[1]);

          this.removeUnnecessaryBrackets(convertedExpression);

          this.replaceValues(convertedExpression, runs);

          Object leftArithmeticResult = this.arithmeticFunctions(convertedExpression.getLeft());
          System.out.println("Left Result = " + leftArithmeticResult + ", class = " + leftArithmeticResult.getClass());

          convertedExpression.setLeftObject(leftArithmeticResult);

          Object rightArithmeticResult = this.arithmeticFunctions(convertedExpression.getRight());
          System.out.println("Right Result = " + rightArithmeticResult + ", class = " + rightArithmeticResult.getClass());

          convertedExpression.setRightObject(rightArithmeticResult);

          Boolean eval = convertedExpression.evaluate();
          convertedExpression.setResult(eval);
          logger.info(
              "Result for expression " + convertedExpression.getOriginExpressionString() + " = " + convertedExpression
                  .getResult() + " ("
                  + convertedExpression.toString() + ")");
          String originalExpression = convertedExpression.getOriginExpressionString();
          originalExpression = this.removeAllWhitespaces(originalExpression);
          this.boolRules = this.boolRules.replace(originalExpression, eval.toString());

        }
      }
*//*      throw new EvaluationException("Operator is not supported! Please use one of those: " + Arrays
              .toString(this.operators));*//*

    }

    BooleanEvaluator booleanEvaluator = new BooleanEvaluator();

    logger.debug("Final Boolean String = " + this.boolRules);

    try {

      Boolean result = booleanEvaluator.evaluate(this.boolRules);

      if (result) {
        logger.info("Result for rule " + rule + " = " + result.toString());
        return action.replaceAll(Pattern.quote(" "), "");
      } else {
        logger.info("Result for rule " + rule + " = " + result.toString());
      }

    } catch (java.lang.RuntimeException e) {
      throw new EvaluationException("Expression " + this.boolRules + " malformed");
    }

    return "NO_RESULT";*/

  }


  private List<String> extractExpressions(String leftRight) {
    List<String> allExpressions = new ArrayList();

    String[] orExpressions = this.splitByStr(leftRight, "|");

    for (String orExpression : orExpressions) {
      String[] andExpressions = this.splitByStr(orExpression, "&");
      for (String andExpression : andExpressions) {

        String[] all = this.splitByStr(andExpression, "!(");

        for (String exp : all) {

          //exp = exp.replaceAll(Pattern.quote("("), "");
          //exp = exp.replaceAll(Pattern.quote(")"), "");

          allExpressions.add(exp);
        }
      }
    }

    return allExpressions;

  }


  private Object arithmeticFunctions(String exp) {


        String[] numbers = exp.split("[^-?0-9.]");

        Set<String> numbersSet = new HashSet<>();

        for(String n: numbers) {
          numbersSet.add(n);
        }

        for (String n : numbersSet) {
          System.out.println("Number: " + n);

          if (n != null && !n.equals("") && !n.contains(".")) {

            exp = exp.replace(n, n + ".0");
            System.out.println("new part = " + exp);

          }
        }

        if(exp.contains("*") || exp.contains("/")) {

          ExpressionParser parser = new SpelExpressionParser();

          System.out.println("Apply arithmetic functions");
          BigDecimal bd = parser.parseExpression(exp).getValue(BigDecimal.class);
          System.out.println("Result = " + bd.toString());
          return bd;
        }
        else {

          try {
            BigDecimal bd2 = new BigDecimal(this.removeAllWhitespaces(exp));

            return bd2;
          }
          catch (NumberFormatException e) {
            return exp;
          }

        }

  }



  private String[] splitByStr(String part, String str) {
    return part.split(Pattern.quote(str));
  }

  private String replaceValues(String expression, ExecutionRuns runs) throws EvaluationException {


    List<String> objects = this.findObjectStrings(expression);

    System.out.println("Found " + objects.size() + " objects ");

    for(String str : objects) {
      Object o = this.getObject(str, runs);

      if(o instanceof Number) {
        if(!o.toString().contains(".")) {
          o = o.toString() + ".0";
        }
      }
      else if(!o.toString().contains("'")) {
        o = "'"+o.toString()+"'";
      }

      System.out.println("Object = " + o);

      System.out.println(str);
      expression = expression.replace(str, o.toString());


      // expression.setLeftObject(getDataType(o));
    }

    logger.debug("replaced expression: " + expression);

    return expression;

  }


  /*private void replaceValues(Expression expression, ExecutionRuns runs) throws EvaluationException {
    logger.debug("Expression before replacement: " + expression.toString());
    String left = expression.getLeft();
    if (left.contains("{")) {

      List<String> objects = this.findObjectStrings(left);

      System.out.println("Found " + objects.size() + " objects ");

      for(String str : objects) {
        Object o = this.getObject(str, runs);

        System.out.println("Object = " + o);

        System.out.println(str);
        expression.setLeft(expression.getLeft().replace(str, o.toString()));

       // expression.setLeftObject(getDataType(o));
      }


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
  }*/


  private List<String> findObjectStrings(String str) {

    Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // (.*?) means 'anything'

    Matcher matcher = pattern.matcher(str); //Note: replace <str> with your string variable, which contains the <str> and </str> codes (and the text between them).

    List<String> objectStrings = new ArrayList<>();

    while(matcher.find()) { // Loops every time the matcher has an occurrence , then adds that to the occurrence string.

      System.out.println(matcher.group(0));
      objectStrings.add(matcher.group(0)); //Change the ", " to anything that you want to separate the occurrence by.
    }

    return objectStrings;

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

  private void removeUnnecessaryBrackets(Expression exp) {

    logger.debug("Before removing unnecessary brackets " + exp.toString());

    Integer leftCountLeftExp = this.countBrackets(exp.getLeft(), "(");
    Integer rightCountLeftExp = this.countBrackets(exp.getLeft(), ")");

    Integer leftCountRightExp = this.countBrackets(exp.getRight(), "(");
    Integer rightCountRightExp = this.countBrackets(exp.getRight(), ")");

    if(leftCountLeftExp > rightCountLeftExp || leftCountLeftExp < rightCountLeftExp
    || leftCountRightExp > rightCountRightExp || leftCountRightExp < rightCountRightExp
    ) {


      if(leftCountLeftExp > rightCountLeftExp) {
        Integer diff = leftCountLeftExp - rightCountLeftExp;

        for(int i = 0; i < diff; i++) {

          exp.setLeft(exp.getLeft().replace("(", ""));
        }

      }
      else if (leftCountLeftExp < rightCountLeftExp) {
        Integer diff = rightCountLeftExp - leftCountLeftExp;
        for(int i = 0; i < diff; i++) {



          exp.setLeft(exp.getLeft().replace(")", ""));
        }
      }

      if(leftCountRightExp > rightCountRightExp) {
        Integer diff = leftCountRightExp - rightCountRightExp;

        for(int i = 0; i < diff; i++) {
          exp.setRight(exp.getRight().replace("(", ""));
        }

      }
      else if (leftCountRightExp < rightCountRightExp) {
        Integer diff = rightCountRightExp - leftCountRightExp;
        for(int i = 0; i < diff; i++) {
          exp.setRight(exp.getRight().replace(")", ""));
        }
      }


      logger.warn("Removed unnecessary round brackets in expression " + exp.getOriginExpressionString());


    }






    logger.debug("After removing unnecessary brackets " + exp.toString());


  }

  private Integer countBrackets(String str, String bracket){

    return  StringUtils.countOccurrencesOf(str, bracket);

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
