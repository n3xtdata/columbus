package com.n3xtdata.columbus.evaluation.booleanevaluator.ast.nonterminal;

import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.BooleanExpression;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.NonTerminal;

public class Not extends NonTerminal {

  public void setChild(BooleanExpression child) {
    setLeft(child);
  }

  public void setRight(BooleanExpression right) {
    throw new UnsupportedOperationException();
  }

  public boolean interpret() {
    return !left.interpret();
  }

  public String toString() {
    return String.format(" ! %s", left);
  }
}

