package com.ted.resonance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class ExCfgActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer beginBlock;
    private Integer endBlock;
    private Integer period;
    private BigDecimal eth;
    private BigDecimal etc;
    private Integer status;
    private String remark;
    private Date createTime;
    private Date updateTime;
    private Integer del;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String toString(){
        return "excfgactivity--id:"+this.getId()+"bengin:"+this.getBeginBlock()+"end:"+this.getEndBlock();
    }
}

