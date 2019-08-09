package com.ted.resonance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String address;
    @ApiModelProperty(notes = "折扣，0.7 = 7折")
    private Float discount;
    @ApiModelProperty(notes = "0可用， 1未生效， 2已失效， 3已使用")
    private Integer status;
    @ApiModelProperty(notes = "额度限制")
    @Column(columnDefinition = "decimal(30,18)")
    private BigDecimal payment;
    @JsonIgnore
    private Long leftTime;
    @JsonIgnore
    private Integer transactionId;
    private String type;
    @ApiModelProperty(notes = "获得的ted回报")
    @Column(columnDefinition = "decimal(30,0)")
    private BigDecimal reward;
    @JsonIgnore
    private Date beginTime;
    @ApiModelProperty(notes = "优惠券失效时间", example = "1561775339000")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date endTime;
    @ApiModelProperty(notes = "优惠券创建时间", example = "1561429714000")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAt;
    @UpdateTimestamp
    @JsonIgnore
    private Date updatedAt;
    @JsonIgnore
    private Integer beginBlock;
    @ApiModelProperty("优惠券结束区块")
    private Integer endBlock;
    @JsonIgnore
    private Integer leftBlock;

    private String v;
    private String r;
    private String s;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(Long leftTime) {
        this.leftTime = leftTime;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public Integer getBeginBlock() {
        return beginBlock;
    }

    public void setBeginBlock(Integer beginBlock) {
        this.beginBlock = beginBlock;
    }

    public Integer getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(Integer endBlock) {
        this.endBlock = endBlock;
    }

    public Integer getLeftBlock() {
        return leftBlock;
    }

    public void setLeftBlock(Integer leftBlock) {
        this.leftBlock = leftBlock;
    }
}
