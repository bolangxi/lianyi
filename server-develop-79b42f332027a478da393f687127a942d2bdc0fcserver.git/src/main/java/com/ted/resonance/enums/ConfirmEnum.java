package com.ted.resonance.enums;

/**
 * @Auther: zzm
 * @Date: 2019-07-16 17:34
 * @Description:
 */
public enum ConfirmEnum {

    CONFIRMING(0,"待确认"),
    SUCCESS(1,"成功"),
    FAIL(2,"失败");

    private int code;
    private String name;

    ConfirmEnum(int code, String name) {
        this.code=code;
        this.name = name;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
