package com.ted.resonance.entity.response;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigInteger;

public class WithdrawSignResponse {
    @ApiModelProperty("v")
    private BigInteger v;
    @ApiModelProperty("r")
    private String r;
    @ApiModelProperty("s")
    private String s;
    @ApiModelProperty("订单号")
    private String tradeNo;

    public BigInteger getV() {
        return v;
    }

    public void setV(BigInteger v) {
        this.v = v;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public String toString() {
        return "WithdrawSignResponse{" +
                "v=" + v +
                ", r='" + r + '\'' +
                ", s='" + s + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                '}';
    }
}
