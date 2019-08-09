package com.ted.resonance.entity.valueobject;

import io.swagger.annotations.ApiModelProperty;

public class Nonce {
    @ApiModelProperty("该地址在etc链的 nonce")
    private Integer etcNonce;
    @ApiModelProperty("该地址在etc链的 nonce")
    private Integer ethNonce;

    public Integer getEtcNonce() {
        return etcNonce;
    }

    public void setEtcNonce(Integer etcNonce) {
        this.etcNonce = etcNonce;
    }

    public Integer getEthNonce() {
        return ethNonce;
    }

    public void setEthNonce(Integer ethNonce) {
        this.ethNonce = ethNonce;
    }
}
