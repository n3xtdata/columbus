package com.n3xtdata.columbus.evaluation.booleanevaluator.ast.terminal;

import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.Terminal;

public class True extends Terminal {

  public True() {
    super(true);
  }

  public boolean interpret() {
    return value;
  }
}
