package com.ted.resonance.enums;

public enum WithdrawOrderRecordStatusEnum {
    NOT_FROZEN(1, "未冻结"),
    PENDING(2, "待审批"),
    PAYING(3, "打币中"),//人工/自动审批已通过，区块确认打币中
    SUCCESS(4, "成功"),//提币操作流程已完成，且已将币成功提转至提币地址/钱包
    REJECTED(5, "拒绝"),//1、人工审核：拒绝
    FAILURE(6, "失败"),//交易打包到区块中，但是交易状态是失败
    BLOCKCHAIN_CONFIRM(7, "问题订单"),//问题订单（重新提币）
    INVALID(8, "单据作废"),
    AUTO_CONFIRM(9, "程序待确认");

    WithdrawOrderRecordStatusEnum(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    private Integer index;
    private String name;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
