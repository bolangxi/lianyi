package com.ted.resonance.enums;

/**
 * @Auther: zzm
 * @Date: 2019-07-16 17:34
 * @Description:
 */
public enum ActivityStatusEnum {

    NOT_START(0,"未开始"),
    STARTING(1,"进行中"),
    NOT_REWARD(2,"已结束奖金未发放"),
    HAS_REWARD(3,"已结束奖金已发放");
    private int code;
    private String name;


    ActivityStatusEnum(int code,String name) {
        this.code=code;
        this.name = name;
    }
    public static ActivityStatusEnum getNameByCode(Integer code) {
        for (ActivityStatusEnum activityStatusEnum : ActivityStatusEnum.values()) {
            if (code.equals(activityStatusEnum.getCode())) {
                return activityStatusEnum;
            }
        }
        return null;
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
