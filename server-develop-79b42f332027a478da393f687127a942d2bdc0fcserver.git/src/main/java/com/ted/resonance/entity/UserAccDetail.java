package com.ted.resonance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class UserAccDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private Long accId;
  private String addr;
  private String tradeNo;
  private String coinType;
  private BigDecimal amount;
  private BigDecimal preAmount;
  private BigDecimal afterAmount;
  private BigDecimal fee;
  private String tradeType;
  private String tradeStatus;
  private String operateType;
  private Date createTime;
  private Date updateTime;
  private String remark;
  private Integer isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getAccId() {
    return accId;
  }

  public void setAccId(Long accId) {
    this.accId = accId;
  }

  public String getTradeNo() {
    return tradeNo;
  }

  public void setTradeNo(String tradeNo) {
    this.tradeNo = tradeNo;
  }

  public String getCoinType() {
    return coinType;
  }

  public void setCoinType(String coinType) {
    this.coinType = coinType;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public BigDecimal getPreAmount() {
    return preAmount;
  }

  public void setPreAmount(BigDecimal preAmount) {
    this.preAmount = preAmount;
  }

  public BigDecimal getAfterAmount() {
    return afterAmount;
  }

  public void setAfterAmount(BigDecimal afterAmount) {
    this.afterAmount = afterAmount;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public String getTradeStatus() {
    return tradeStatus;
  }

  public void setTradeStatus(String tradeStatus) {
    this.tradeStatus = tradeStatus;
  }

  public String getOperateType() {
    return operateType;
  }

  public void setOperateType(String operateType) {
    this.operateType = operateType;
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Integer isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getAddr() {
    return addr;
  }

  public void setAddr(String addr) {
    this.addr = addr;
  }
}
