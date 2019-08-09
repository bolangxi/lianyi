package com.ted.resonance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ExTedBurn {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long activityId;
  private Long userId;
  private String addr;
  private String txHash;
  private Integer blockHeight;
  private String blockHash;
  private BigDecimal amount;
  private BigDecimal contributionDegree;
  private Integer calStatus;
  private Integer status;
  private Date createTime;
  private Date updateTime;
  private String isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }

  public String getTxHash() {
    return txHash;
  }

  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }

  public Integer getBlockHeight() {
    return blockHeight;
  }

  public void setBlockHeight(Integer blockHeight) {
    this.blockHeight = blockHeight;
  }

  public String getBlockHash() {
    return blockHash;
  }

  public void setBlockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getContributionDegree() {
    return contributionDegree;
  }

  public void setContributionDegree(BigDecimal contributionDegree) {
    this.contributionDegree = contributionDegree;
  }

  public Integer getCalStatus() {
    return calStatus;
  }

  public void setCalStatus(Integer calStatus) {
    this.calStatus = calStatus;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(String isDeleted) {
    this.isDeleted = isDeleted;
  }
}
