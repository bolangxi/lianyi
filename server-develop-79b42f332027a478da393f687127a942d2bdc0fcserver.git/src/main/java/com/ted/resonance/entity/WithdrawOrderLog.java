package com.ted.resonance.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class WithdrawOrderLog implements Serializable {
    private BigInteger id;
    private BigInteger orderId;
    private String currency;
    private Date createTime;
    private String operatorId;
    private String event;
    private String remark;
    private String parentCurrency;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParentCurrency() {
        return parentCurrency;
    }

    public void setParentCurrency(String parentCurrency) {
        this.parentCurrency = parentCurrency;
    }
}
