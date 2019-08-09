package com.ted.resonance.enums;

/**
 * @Auther: zzm
 * @Date: 2019-07-16 17:34
 * @Description:计算贡献值状态枚举
 */
public enum ContributionCalStatus {

    NOT_CAL(0,"未计算"),
    HAS_CAL(1,"已计算");
    private int code;
    private String name;
    ContributionCalStatus(int code, String name) {
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
