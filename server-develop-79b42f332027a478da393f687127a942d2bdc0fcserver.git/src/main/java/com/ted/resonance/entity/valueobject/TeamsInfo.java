package com.ted.resonance.entity.valueobject;

import com.ted.resonance.entity.Team;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class TeamsInfo {
    @ApiModelProperty(notes = "阶段，1第一期，2第二期")
    private Integer phase;
    @ApiModelProperty(notes = "0未开始， 1进行中，2已结束：中奖， 3已结束：未中奖，4已结束:已领奖， 5 已结束：未开奖")
    private Integer status;

    @ApiModelProperty(notes = "etc奖金池额度")
    private BigDecimal etcBonusPool;

    @ApiModelProperty("eth奖金池额度")
    private BigDecimal ethBonusPool;

    @ApiModelProperty(notes = "前二十名骑士团信息")
    private List<Team> topTwentyTeams;

    @ApiModelProperty(notes = "我的骑士团信息")
    private Team myTeam;

    @ApiModelProperty("状态为未开始时，剩余区块")
    private Integer leftBlock;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getEtcBonusPool() {
        return etcBonusPool;
    }

    public void setEtcBonusPool(BigDecimal etcBonusPool) {
        this.etcBonusPool = etcBonusPool;
    }

    public BigDecimal getEthBonusPool() {
        return ethBonusPool;
    }

    public void setEthBonusPool(BigDecimal ethBonusPool) {
        this.ethBonusPool = ethBonusPool;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public List<Team> getTopTwentyTeams() {
        return topTwentyTeams;
    }

    public void setTopTwentyTeams(List<Team> topTwentyTeams) {
        this.topTwentyTeams = topTwentyTeams;
    }

    public Team getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(Team myTeam) {
        this.myTeam = myTeam;
    }


    public Integer getLeftBlock() {
        return leftBlock;
    }

    public void setLeftBlock(Integer leftBlock) {
        this.leftBlock = leftBlock;
    }
}
