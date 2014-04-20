package com.manniwood.mmpt.test.factory;

import org.testng.annotations.Factory;

import com.manniwood.mmpt.test.IntegerArrayTest2;
import com.manniwood.mmpt.test.UUIDTest;

public class TestNGTestFactory {
    @Factory
    public Object[] allTests() {
        return new Object[] {
                new UUIDTest(),
                new IntegerArrayTest2()
        };
    }

}
