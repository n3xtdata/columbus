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

import com.n3xtdata.columbus.evaluation.exceptions.EvaluationException;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Expression {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private String left;
  private Object leftObject;
  private String operator;
  private String right;
  private Object rightObject;
  private String action;
  private String originExpressionString;

  Expression(String left, String operator, String right, String action) {
    this.originExpressionString = left+operator+right;
    this.left = left;
    this.operator = operator.replaceAll(" ", "");
    this.right = right;
    this.action = action.replaceAll(" ", "");

    System.out.println("origin: " + this.originExpressionString);

  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getRight() {
    return right;
  }

  public void setRight(String right) {
    this.right = right;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Object getLeftObject() {
    return leftObject;
  }

  public void setLeftObject(Object leftObject) {
    this.leftObject = leftObject;
  }

  public Object getRightObject() {
    return rightObject;
  }

  public void setRightObject(Object rightObject) {
    this.rightObject = rightObject;
  }

  public Boolean evaluate() throws EvaluationException {
    if (leftObject.getClass() != rightObject.getClass()) {
      throw new EvaluationException("Cannot compare two different classes");
    }
    switch (operator) {
      case "==":
        return equals(leftObject, rightObject);
      case "!=":
        return !equals(leftObject, rightObject);
      case "<":
        return lessThan(leftObject, rightObject);
      case "<=":
        return lessEqualsThan(leftObject, rightObject);
      case ">":
        return greaterThan(leftObject, rightObject);
      case ">=":
        return greaterEqualsThan(leftObject, rightObject);
      default:
        return false;
    }
  }


  public String getOriginExpressionString() {
    return this.originExpressionString;
  }

  @Override
  public String toString() {
    return "Expression{" +
            "left='" + left + '\'' +
            ", leftObject=" + leftObject +
            ", operator='" + operator + '\'' +
            ", right='" + right + '\'' +
            ", rightObject=" + rightObject +
            ", action='" + action + '\'' +
            ", originExpressionString='" + originExpressionString + '\'' +
            '}';
  }

  private Boolean equals(Object l, Object r) {
    return compareValue(l, r) == 0;
  }

  private Boolean lessThan(Object l, Object r) {
    return compareValue(l, r) < 0;
  }

  private Boolean greaterThan(Object l, Object r) {
    return compareValue(l, r) > 0;
  }

  private Boolean lessEqualsThan(Object l, Object r) {
    return compareValue(l, r) <= 0;
  }

  private Boolean greaterEqualsThan(Object l, Object r) {
    return compareValue(l, r) >= 0;
  }

  private Integer compareValue(Object l, Object r) {
    logger.debug("Comparing: " + l.toString() + " & " + r.toString());
    logger.debug("Datatypes: " + l.getClass() + " & " + r.getClass());
    if (leftObject instanceof BigDecimal) {
      BigDecimal n1 = new BigDecimal(leftObject.toString());
      BigDecimal n2 = new BigDecimal(rightObject.toString());
      return n1.compareTo(n2);
    } else {
      return l.toString().compareTo(r.toString());
    }
  }

}
