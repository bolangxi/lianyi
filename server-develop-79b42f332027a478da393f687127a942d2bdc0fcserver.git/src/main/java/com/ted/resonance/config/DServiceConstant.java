package com.ted.resonance.config;

/**
 * 座右铭：求真、务实
 *
 * @Author： Gread
 * @Date： Created in 下午7:32 2019/7/16
 * @Description：
 */
public enum DServiceConstant {
    // 参数错误
    PARAMS_ERROR(400, "paramError"),
    USER_ERROR(10001, "userError"),
    ADDRESS_NOT_CORRENCT(10002, "addressNotCorrect"),
    COUPONS_NOT_EXIST(10003, "couponsNotExist"),
    NO_ACTIVITY_INFO(1004,"activityNotExist"),
    BLOCK_ERROR(1005,"BlockError"),
    SUBAWARD_ERROR(1006,"SubAwardError"),
    SUBAWARD_UNCAL_ERROR(1007,"SubAwardUnCalError"),
    REWARD_FAIL(1008, "rewardFail"),
    SEND_TX_FAILED(501, "sendTxFailed");


    private int code;
    private String msg;

    DServiceConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
