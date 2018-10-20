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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

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



  private Object getValue(String componentName, String field, ExecutionRuns runs) {
    return runs.get(componentName).get(0).get(field);
  }



  public void setAllRules(String allRules) {
    this.allRules = allRules;
  }

  @Override
  public Boolean validate(Integer componentSize) {
    return true;
  }

}
