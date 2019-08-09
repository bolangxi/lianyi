package com.ted.resonance.entity.request;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class WithdrawApplyRequest {

    /**
     * 提币金额
     */
    @ApiModelProperty("提币金额")
    private BigDecimal amount;

    /**
     * 币种 ETH ETC
     */
    @ApiModelProperty("币种 ETH ETC")
    private String coinType;

    /**
     * 目标地址
     */
    @ApiModelProperty("目标地址")
    private String toAddress;


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    @Override
    public String toString() {
        return "WithdrawApplyRequest{" +
                "amount=" + amount +
                ", coinType='" + coinType + '\'' +
                ", toAddress='" + toAddress + '\'' +
                '}';
    }
}
