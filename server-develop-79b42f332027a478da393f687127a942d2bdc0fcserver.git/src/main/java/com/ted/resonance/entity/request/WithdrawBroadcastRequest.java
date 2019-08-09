package com.ted.resonance.entity.request;

import io.swagger.annotations.ApiModelProperty;

public class WithdrawBroadcastRequest {
    @ApiModelProperty("签名数据")
    private String signedData;
    @ApiModelProperty("订单号")
    private String tradeNo;

    public String getSignedData() {
        return signedData;
    }

    public void setSignedData(String signedData) {
        this.signedData = signedData;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public String toString() {
        return "WithdrawBroadcastRequest{" +
                "signedData='" + signedData + '\'' +
                ", tradeNo='" + tradeNo + '\'' +
                '}';
    }
}
