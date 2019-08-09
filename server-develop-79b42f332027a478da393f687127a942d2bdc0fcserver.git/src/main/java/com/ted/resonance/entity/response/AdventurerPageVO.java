package com.ted.resonance.entity.response;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class AdventurerPageVO {

    @ApiModelProperty(notes = "活动进度",example ="60")
    private Double activitySchedule;
    @ApiModelProperty(notes = "加成剩余区块")
    private String surplus;
    @ApiModelProperty(notes = "加成百分比", example = "10")
    private Double addRate;
    @ApiModelProperty(notes = "矿工费")
    private String minersFees;
    @ApiModelProperty(notes = "我的贡献值")
    private Double myContributionDegree;
    @ApiModelProperty(notes = "活动未开始提示信息", example = "活动即将开始,敬请期待!")
    private String msgTxt;
    @ApiModelProperty(notes = "eth奖值")
    private BigDecimal eth;
    @ApiModelProperty(notes = "etc奖值")
    private BigDecimal etc;
    @ApiModelProperty(notes = "活动结束配置块高")
    private Integer endBlock;
    @ApiModelProperty(notes = "活动状态[0:未开始 1:进行中 2:已结束奖金未发 3:已结束奖金已发]")
    private Integer status;
    @ApiModelProperty(notes = "ETH预计可得分")
    private BigDecimal expectEth;
    @ApiModelProperty(notes = "ETC预计可得分")
    private BigDecimal expectEtc;
    @ApiModelProperty(notes = "gaslimit")
    private Integer gasLimit;
    @ApiModelProperty(notes = "gasPrice")
    private Integer gasPrice;
    @ApiModelProperty(notes = "当前钱包ted数量")
    private BigDecimal ted;
    @ApiModelProperty(notes = "活动ID")
    private Long activityId;
    @ApiModelProperty(notes = "冒险家合约地址")
    private String exContractAddr;
    @ApiModelProperty(notes = "TED合约地址")
    private String tedContractAddr;

    public Double getActivitySchedule() {
        return activitySchedule;
    }

    public void setActivitySchedule(Double activitySchedule) {
        this.activitySchedule = activitySchedule;
    }

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public Double getAddRate() {
        return addRate;
    }

    public void setAddRate(Double addRate) {
        this.addRate = addRate;
    }

    public String getMinersFees() {
        return minersFees;
    }

    public void setMinersFees(String minersFees) {
        this.minersFees = minersFees;
    }

    public Double getMyContributionDegree() {
        return myContributionDegree;
    }

    public void setMyContributionDegree(Double myContributionDegree) {
        this.myContributionDegree = myContributionDegree;
    }

    public String getMsgTxt() {
        return msgTxt;
    }

    public void setMsgTxt(String msgTxt) {
        this.msgTxt = msgTxt;
    }

    public BigDecimal getEth() {
        return eth;
    }

    public void setEth(BigDecimal eth) {
        this.eth = eth;
    }

    public BigDecimal getEtc() {
        return etc;
    }

    public void setEtc(BigDecimal etc) {
        this.etc = etc;
    }

    public Integer getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(Integer endBlock) {
        this.endBlock = endBlock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getExpectEth() {
        return expectEth;
    }

    public void setExpectEth(BigDecimal expectEth) {
        this.expectEth = expectEth;
    }

    public BigDecimal getExpectEtc() {
        return expectEtc;
    }

    public void setExpectEtc(BigDecimal expectEtc) {
        this.expectEtc = expectEtc;
    }

    public Integer getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Integer gasLimit) {
        this.gasLimit = gasLimit;
    }

    public Integer getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Integer gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getTed() {
        return ted;
    }

    public void setTed(BigDecimal ted) {
        this.ted = ted;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getExContractAddr() {
        return exContractAddr;
    }

    public void setExContractAddr(String exContractAddr) {
        this.exContractAddr = exContractAddr;
    }

    public String getTedContractAddr() {
        return tedContractAddr;
    }

    public void setTedContractAddr(String tedContractAddr) {
        this.tedContractAddr = tedContractAddr;
    }
}
