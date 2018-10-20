package com.n3xtdata.columbus.evaluation.booleanevaluator.ast.nonterminal;

import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.NonTerminal;

public class Or extends NonTerminal {

  public boolean interpret() {
    return left.interpret() || right.interpret();
  }

  public String toString() {
    return String.format("(%s | %s)", left, right);
  }
}
