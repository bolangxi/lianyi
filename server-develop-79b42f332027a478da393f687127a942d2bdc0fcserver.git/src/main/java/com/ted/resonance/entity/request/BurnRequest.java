package com.ted.resonance.entity.request;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class BurnRequest {
    @ApiModelProperty(value = "已簽名的授权交易字符串")
    private String signedApproveData;
    @ApiModelProperty(value = "已簽名的燃烧交易字符串")
    private String signedBrunData;
    @ApiModelProperty(value = "燃燒的ted量， 注意精度，是区块浏览器看到的数值")
    private BigDecimal amount;
    @ApiModelProperty(value = "出账地址")
    private String address;
    @ApiModelProperty(value = "出账方的用户id")
    private Long userId;
    @ApiModelProperty(value = "活动id")
    private Long activityId;

    public String getSignedApproveData() {
        return signedApproveData;
    }

    public void setSignedApproveData(String signedApproveData) {
        this.signedApproveData = signedApproveData;
    }

    public String getSignedBrunData() {
        return signedBrunData;
    }

    public void setSignedBrunData(String signedBrunData) {
        this.signedBrunData = signedBrunData;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
