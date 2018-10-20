package com.n3xtdata.columbus.evaluation.booleanevaluator.ast.terminal;


import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.Terminal;

public class False extends Terminal {

  public False() {
    super(false);
  }

  public boolean interpret() {
    return value;
  }
}