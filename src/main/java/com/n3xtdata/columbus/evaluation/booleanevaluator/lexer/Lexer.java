package com.n3xtdata.columbus.evaluation.booleanevaluator.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;

public class Lexer {

  public static final int EOL = -3;
  public static final int EOF = -2;
  public static final int INVALID = -1;
  public static final int NONE = 0;
  public static final int OR = 1;
  public static final int AND = 2;
  public static final int NOT = 3;
  public static final int TRUE = 4;
  public static final int FALSE = 5;
  public static final int LEFT = 6;
  public static final int RIGHT = 7;
  public static final String TRUE_LITERAL = "true";
  public static final String FALSE_LITERAL = "false";
  private StreamTokenizer input;
  private int symbol = NONE;

  public Lexer(InputStream in) {
    Reader r = new BufferedReader(new InputStreamReader(in));
    input = new StreamTokenizer(r);
    input.resetSyntax();
    input.wordChars('a', 'z');
    input.wordChars('A', 'Z');
    input.whitespaceChars('\u0000', ' ');
    input.whitespaceChars('\n', '\t');
    input.ordinaryChar('(');
    input.ordinaryChar(')');
    input.ordinaryChar('&');
    input.ordinaryChar('|');
    input.ordinaryChar('?');
  }

  public int nextSymbol() {
    try {
      switch (input.nextToken()) {
        case StreamTokenizer.TT_EOL:
          symbol = EOL;
          break;
        case StreamTokenizer.TT_EOF:
          symbol = EOF;
          break;
        case StreamTokenizer.TT_WORD: {
            if (input.sval.equalsIgnoreCase(TRUE_LITERAL)) {
                symbol = TRUE;
            } else if (input.sval.equalsIgnoreCase(FALSE_LITERAL)) {
                symbol = FALSE;
            }
          break;
        }
        case '(':
          symbol = LEFT;
          break;
        case ')':
          symbol = RIGHT;
          break;
        case '&':
          symbol = AND;
          break;
        case '|':
          symbol = OR;
          break;
        case '!':
          symbol = NOT;
          break;
        default:
          symbol = INVALID;
      }
    } catch (IOException e) {
      symbol = EOF;
    }
    return symbol;
  }
}
