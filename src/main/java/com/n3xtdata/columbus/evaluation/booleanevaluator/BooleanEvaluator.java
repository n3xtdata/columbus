package com.n3xtdata.columbus.evaluation.booleanevaluator;

import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.BooleanExpression;
import com.n3xtdata.columbus.evaluation.booleanevaluator.lexer.Lexer;
import com.n3xtdata.columbus.evaluation.booleanevaluator.parser.RecursiveDescentParser;
import java.io.ByteArrayInputStream;


public class BooleanEvaluator {

  public Boolean evaluate(String expression) {
    Lexer lexer = new Lexer(new ByteArrayInputStream(expression.getBytes()));
    RecursiveDescentParser parser = new RecursiveDescentParser(lexer);
    BooleanExpression ast = parser.build();

    return ast.interpret();
  }
}

