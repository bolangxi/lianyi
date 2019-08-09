package com.ted.resonance.entity.request;

import io.swagger.annotations.ApiModelProperty;

public class BalanceRequest {
    // 币种类型
    @ApiModelProperty(notes = "币种类型（ETH/ETC）")
    private String coinType;
    // 地址
    @ApiModelProperty(notes = "地址")
    private String addr;

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
