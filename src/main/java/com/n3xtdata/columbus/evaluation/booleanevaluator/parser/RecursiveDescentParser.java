package com.n3xtdata.columbus.evaluation.booleanevaluator.parser;

import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.BooleanExpression;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.nonterminal.And;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.nonterminal.Not;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.nonterminal.Or;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.terminal.False;
import com.n3xtdata.columbus.evaluation.booleanevaluator.ast.terminal.True;
import com.n3xtdata.columbus.evaluation.booleanevaluator.lexer.Lexer;

public class RecursiveDescentParser {

  private final True t = new True();
  private final False f = new False();
  private Lexer lexer;
  private int symbol;
  private BooleanExpression root;

  public RecursiveDescentParser(Lexer lexer) {
    this.lexer = lexer;
  }

  public BooleanExpression build() {
    expression();
    return root;
  }

  private void expression() {
    term();
    while (symbol == Lexer.OR) {
      Or or = new Or();
      or.setLeft(root);
      term();
      or.setRight(root);
      root = or;
    }
  }

  private void term() {
    factor();
    while (symbol == Lexer.AND) {
      And and = new And();
      and.setLeft(root);
      factor();
      and.setRight(root);
      root = and;
    }
  }

  private void factor() {
    symbol = lexer.nextSymbol();
    if (symbol == Lexer.TRUE) {
      root = t;
      symbol = lexer.nextSymbol();
    } else if (symbol == Lexer.FALSE) {
      root = f;
      symbol = lexer.nextSymbol();
    } else if (symbol == Lexer.NOT) {
      Not not = new Not();
      factor();
      not.setChild(root);
      root = not;
    } else if (symbol == Lexer.LEFT) {
      expression();
      symbol = lexer.nextSymbol(); // we don't care about ')'
    } else {
      throw new RuntimeException("Expression Malformed");
    }
  }
}