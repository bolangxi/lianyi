package com.ted.resonance.enums;

/**
 * @Auther: zzm
 * @Date: 2019-07-16 17:34
 * @Description:
 */
public enum RewardStatusEnum {

    INIT("1","待领取"),
    RECEIVED("2","已领取"),
    USEING("3","使用中"),
    USED("4","已使用"),
    EXPIRE("5","已过期");
    private String code;
    private String name;

    RewardStatusEnum(String code, String name) {
        this.code=code;
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
    }}
