package com.ted.resonance.enums;

public enum OperateType {
    ADD("+", "加法"),
    SUB("-", "减法");

    private String code;
    private String name;

    OperateType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
