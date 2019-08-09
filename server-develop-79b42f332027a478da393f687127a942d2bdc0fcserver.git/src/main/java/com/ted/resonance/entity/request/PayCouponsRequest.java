package com.ted.resonance.entity.request;

import io.swagger.annotations.ApiModelProperty;

public class PayCouponsRequest {
    @ApiModelProperty(value = "已簽名的授权交易字符串")
    private String signedApproveData;
    @ApiModelProperty(value = "已簽名的炼金交易字符串")
    private String signedCouponsData;
    @ApiModelProperty(notes = "地址信息")
    private String addr;
    @ApiModelProperty(notes = "领金券Id")
    private Long couponsId;

    public String getSignedApproveData() {
        return signedApproveData;
    }

    public void setSignedApproveData(String signedApproveData) {
        this.signedApproveData = signedApproveData;
    }

    public String getSignedCouponsData() {
        return signedCouponsData;
    }

    public void setSignedCouponsData(String signedCouponsData) {
        this.signedCouponsData = signedCouponsData;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Long getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(Long couponsId) {
        this.couponsId = couponsId;
    }
}
