package com.ted.resonance.dto;


import com.ted.resonance.enums.OperateType;
import com.ted.resonance.enums.TradeType;

import java.io.Serializable;
import java.math.BigDecimal;

public class OperateUserAccDto implements Serializable {

    //用户ID
    private Long userId;
    //币种
    private String coinType;
    //交易金额
    private BigDecimal tradeAmount;
    //交易类型枚举
    private TradeType tradeType;
    //操作类型枚举
    private OperateType operateType;
    //用户地址
    private String addr;
    //操作备注
    private String remark;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
