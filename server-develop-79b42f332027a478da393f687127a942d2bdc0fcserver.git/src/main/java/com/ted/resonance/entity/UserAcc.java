package com.ted.resonance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class UserAcc {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  private String addr;
  private String coinType;
  private BigDecimal balance;
  private BigDecimal frozenAmount;
  private Date createTime;
  private Date updateTime;
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

  public String getCoinType() {
    return coinType;
  }

  public void setCoinType(String coinType) {
    this.coinType = coinType;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getFrozenAmount() {
    return frozenAmount;
  }

  public void setFrozenAmount(BigDecimal frozenAmount) {
    this.frozenAmount = frozenAmount;
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
