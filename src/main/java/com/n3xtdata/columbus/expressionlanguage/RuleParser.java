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

package com.n3xtdata.columbus.expressionlanguage;

import com.n3xtdata.columbus.expressionlanguage.exceptions.EvaluationException;
import com.n3xtdata.columbus.executor.ExecutionRuns;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@SuppressWarnings("MagicConstant")
public class RuleParser {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String rules;

  private String[] getSplittedByStr(String part, String str) {
    return part.split(Pattern.quote(str));
  }

  private Boolean isEmptyLine(String line) {
    line = removeAllWhitespaces(line);
    return line.isEmpty();
  }

  private String removeAllWhitespaces(String str) {
    return str.replaceAll(Pattern.quote(" "), "");
  }

  public String parseRule(String rule, ExecutionRuns runs) throws EvaluationException {
    if(isEmptyLine(rule)) {
      return null;
    }
    logger.debug("Parsing rule: " + rule);
    String[] expressionAndAction = getSplittedByStr(rule, "->");

    String expression;
    String pureStatus;

    try {
      expression = expressionAndAction[0];
      pureStatus = removeAllWhitespaces(expressionAndAction[1]);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new EvaluationException("given rule does not match valid syntax");
    }

    expression = replaceValues(expression, runs);
    if (expressionIsTrue(expression)) {
      return pureStatus;
    } else {
      return null;
    }
  }

  private Boolean expressionIsTrue(String expression) throws EvaluationException {
    ExpressionParser parser = new SpelExpressionParser();
    try {
      return parser.parseExpression(expression).getValue(Boolean.class);
    } catch (SpelEvaluationException e) {
      throw new EvaluationException("Parser could not evaluate " + expression);
    }
  }

  private String replaceValues(String expression, ExecutionRuns runs) throws EvaluationException {
    logger.debug("original expression: " + expression);
    Set<String> objects = this.findObjectStrings(expression);

    for (String str : objects) {
      Object o = this.getObject(str, runs);
      if (o instanceof Number) {
        if (!o.toString().contains(".")) {
          o = o.toString() + ".0";
        }
      } else if (!o.toString().contains("'")) {
        o = "'" + o.toString() + "'";
      }
      logger.info("Replaced " + str + " with Object " + o + " Type: " + o.getClass());
      expression = expression.replace(str, o.toString());
    }
    logger.info("new expression: " + expression);
    return expression;
  }


  private Set<String> findObjectStrings(String str) {

    Pattern pattern = Pattern.compile("\\{(.*?)\\}"); // (.*?) means 'anything'
    Matcher matcher = pattern.matcher(str);

    Set<String> objectStrings = new HashSet<>();

    while (matcher.find()) {
      objectStrings.add(matcher.group(0));
    }
    logger.info("Found " + objectStrings.size() + " distinct strings to be replaced: " + Arrays
        .toString(objectStrings.toArray()));
    return objectStrings;
  }

  private Object getObject(String toBeReplaced, ExecutionRuns runs) throws EvaluationException {
    toBeReplaced = removeAllWhitespaces(toBeReplaced);
    String[] componentAndField = getSplittedByStr(removeBrackets(toBeReplaced), ".");
    try {
      String component = componentAndField[0];
      String field = componentAndField[1];
      return runs.getValue(component, field);
    } catch (NullPointerException e) {
      throw new EvaluationException("Could not find given value: " + toBeReplaced);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new EvaluationException(
          "Value to replace is: " + toBeReplaced + " but should be like {componentLabel.field}");
    }
  }

  private String removeBrackets(String str) {
    str = getRemovedStr(str, "{");
    return getRemovedStr(str, "}");
  }

  private String getRemovedStr(String str, String toBeRemoved) {
    return str.replace(toBeRemoved, "");
  }

  public String getRules() {
    return rules;
  }

  public void setRules(String rules) {
    this.rules = rules;
  }

}