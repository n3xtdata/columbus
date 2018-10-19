package com.n3xtdata.columbus.evaluation;

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
        return String.format("!%s", left);
    }
}

