package com.ted.resonance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonIgnore
    private Integer phase;
    @ApiModelProperty(notes = "团长地址")
    private String leaderAddress;
    @ApiModelProperty(notes = "累计etc金额")
    private BigDecimal etcFund;
    @ApiModelProperty(notes = "累计eth金额")
    private BigDecimal ethFund;
    @ApiModelProperty(notes = "人数")
    private Integer number;
    @JsonIgnore
    @CreationTimestamp
    private Date createdAt;
    @JsonIgnore
    @UpdateTimestamp
    private Date updatedAt;


    @ApiModelProperty("团长昵称")
    @Transient
    private String nickname;
    @ApiModelProperty("团长头像地址")
    @Transient
    private String headPicture;
    @ApiModelProperty("排行")
    @Transient
    private Integer rank;
    @ApiModelProperty("是不是我的团")
    @Transient
    private Boolean isMyTeam;
    @ApiModelProperty("国旗地址")
    @Transient
    private String nationUrl;

    @ApiModelProperty("我的领队奖金,当团队为我的时候展示, eth/etc")
    @Transient
    private BigDecimal myLeaderFund;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public String getLeaderAddress() {
        return leaderAddress;
    }

    public void setLeaderAddress(String leaderAddress) {
        this.leaderAddress = leaderAddress;
    }

    public BigDecimal getEtcFund() {
        return etcFund;
    }

    public void setEtcFund(BigDecimal etcFund) {
        this.etcFund = etcFund;
    }

    public BigDecimal getEthFund() {
        return ethFund;
    }

    public void setEthFund(BigDecimal ethFund) {
        this.ethFund = ethFund;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPicture() {
        return headPicture;
    }

    public void setHeadPicture(String headPicture) {
        this.headPicture = headPicture;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Boolean getMyTeam() {
        return isMyTeam;
    }

    public void setMyTeam(Boolean myTeam) {
        isMyTeam = myTeam;
    }

    public String getNationUrl() {
        return nationUrl;
    }

    public void setNationUrl(String nationUrl) {
        this.nationUrl = nationUrl;
    }

    public BigDecimal getMyLeaderFund() {
        return myLeaderFund;
    }

    public void setMyLeaderFund(BigDecimal myLeaderFund) {
        this.myLeaderFund = myLeaderFund;
    }
}
