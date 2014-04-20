package com.manniwood.mmpt.test.beans;

import com.manniwood.mmpt.typehandlers.IntegerArrayWrapper;

public class IntArrayBean {

    private IntegerArrayWrapper integerArrayWrapper;
    private String name;

    public IntArrayBean() {
        // null constructor keeps mybatis happy
    }

    public IntegerArrayWrapper getIntegerArrayWrapper() {
        return integerArrayWrapper;
    }

    public void setIntegerArrayWrapper(IntegerArrayWrapper integerArrayWrapper) {
        this.integerArrayWrapper = integerArrayWrapper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}