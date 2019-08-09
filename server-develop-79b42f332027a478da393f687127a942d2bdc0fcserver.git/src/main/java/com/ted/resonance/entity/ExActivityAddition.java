package com.ted.resonance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class ExActivityAddition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long activityId;
  private Long beginBlock;
  private Long endBlock;
  private Double additionRate;
  private Date createTime;
  private Date updateTime;
  private Long isDeleted;

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

  public Long getBeginBlock() {
    return beginBlock;
  }

  public void setBeginBlock(Long beginBlock) {
    this.beginBlock = beginBlock;
  }

  public Long getEndBlock() {
    return endBlock;
  }

  public void setEndBlock(Long endBlock) {
    this.endBlock = endBlock;
  }

  public Double getAdditionRate() {
    return additionRate;
  }

  public void setAdditionRate(Double additionRate) {
    this.additionRate = additionRate;
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

  public Long getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Long isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String toString(){
    return "ExActivityAddition--id:"+this.getId()+"bengin:"+this.getBeginBlock()+"end:"+this.getEndBlock()+"additionRate:"+this.getAdditionRate();
  }
}
