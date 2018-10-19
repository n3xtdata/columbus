package com.n3xtdata.columbus.evaluation;

public class And extends NonTerminal {
    public boolean interpret() {
        return left.interpret() && right.interpret();
    }

    public String toString() {
        return String.format("(%s & %s)", left, right);
    }
}