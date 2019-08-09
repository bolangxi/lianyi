package com.ted.resonance.entity.response;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class BalanceResponse {
    @ApiModelProperty("地址")
    private String addr;
    @ApiModelProperty("币种类型")
    private String coinType;
    @ApiModelProperty("可用余额")
    private BigDecimal balance;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
