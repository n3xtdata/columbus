package com.n3xtdata.columbus.evaluation;

public class True extends Terminal {
    public True() {
        super(true);
    }

    public boolean interpret() {
        return value;
    }
}
