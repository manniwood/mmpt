package com.manniwood.mmpt.test.beans;


public class IntegerArrayBean {

    private Integer[] integerArray;
    private String name;

    public IntegerArrayBean() {
        // null constructor keeps mybatis happy
    }

    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(Integer[] integerArray) {
        this.integerArray = integerArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}