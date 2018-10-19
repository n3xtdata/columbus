package com.n3xtdata.columbus.evaluation;


public class False extends Terminal {
    public False() {
        super(false);
    }

    public boolean interpret() {
        return value;
    }
}