package com.n3xtdata.columbus.evaluation;

public class Or extends NonTerminal {
    public boolean interpret() {
        return left.interpret() || right.interpret();
    }

    public String toString() {
        return String.format("(%s OR %s)", left, right);
    }
}
