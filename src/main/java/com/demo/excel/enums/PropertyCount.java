package com.demo.excel.enums;

public enum PropertyCount {

    PRODUCT("Product", 3);

    private final String className;
    private final int count;
    PropertyCount(String className, int count) {
        this.className = className;
        this.count = count;
    }

    public String getClassName() {
        return this.className;
    }
    public int getCount() {
        return this.count;
    }
}
