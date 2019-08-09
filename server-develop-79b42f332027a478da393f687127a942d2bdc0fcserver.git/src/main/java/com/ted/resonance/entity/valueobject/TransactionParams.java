package com.ted.resonance.entity.valueobject;

import com.ted.resonance.utils.validator.Coin;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

public class TransactionParams {
    @ApiModelProperty(notes = "ETC/ETH地址")
    @NotBlank
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
    private String address;
    @ApiModelProperty(notes ="类型，限 ETC 或 ETH ,大小不限", example = "etc")
    @Coin
    @NotBlank
    private String type;
    @ApiModelProperty(notes = "支付数量")
    @NotNull
    private BigDecimal payment;
    @ApiModelProperty(notes = "预期获得ted 奖励")
    private BigDecimal reward;
    @ApiModelProperty(notes = "优惠券id ，如果使用了优惠券请添加该字段，否则不用加")
    private Integer couponId;
    @NotBlank
    @ApiModelProperty(notes = "已签名的字符串， 用于交易")
    private String signedString;
    @ApiModelProperty(notes = "邀请人的地址")
    private String inviterAddress;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getSignedString() {
        return signedString;
    }

    public void setSignedString(String signedString) {
        this.signedString = signedString;
    }

    public String getInviterAddress() {
        return inviterAddress;
    }

    public void setInviterAddress(String inviterAddress) {
        this.inviterAddress = inviterAddress;
    }
}
