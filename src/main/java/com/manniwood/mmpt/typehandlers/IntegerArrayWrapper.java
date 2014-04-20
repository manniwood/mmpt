package com.manniwood.mmpt.typehandlers;

public class IntegerArrayWrapper {

    private Integer[] integerArray;

    public IntegerArrayWrapper(Integer[] integerArray) {
        super();
        this.integerArray = integerArray;
    }

    public IntegerArrayWrapper() {
        // empty constructor
    }

    public Integer[] getIntegerArray() {
        return integerArray;
    }

    public void setIntegerArray(Integer[] intArray) {
        this.integerArray = intArray;
    }

}
