package com.manniwood.mmpt.test.beans;
import java.util.UUID;

public class UUIDBean {

    private UUID testId;
    private String name;

    public UUIDBean() {
        // null constructor keeps mybatis happy
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}