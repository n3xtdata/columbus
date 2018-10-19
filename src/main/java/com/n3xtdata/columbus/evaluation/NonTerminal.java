package com.n3xtdata.columbus.evaluation;

public abstract class NonTerminal implements BooleanExpression {
    protected BooleanExpression left, right;

    public void setLeft(BooleanExpression left) {
        this.left = left;
    }

    public void setRight(BooleanExpression right) {
        this.right = right;
    }
}
