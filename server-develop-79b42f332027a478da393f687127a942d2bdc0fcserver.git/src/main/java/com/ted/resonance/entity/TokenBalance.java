package com.ted.resonance.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TokenBalance {
     private int id; //主键ID
     private String contractAddr; //Token合约地址
     private String addr; //用户的ETH地址
     private BigDecimal balance; //Token的余额
     private Date updateTime; //
     private int decimals; //Token精度
     private BigDecimal unconfirmAmount; //Token交易未确认金额
     private String  added; //是否显示在主页：0 不显示，1 显示

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public BigDecimal getUnconfirmAmount() {
        return unconfirmAmount;
    }

    public void setUnconfirmAmount(BigDecimal unconfirmAmount) {
        this.unconfirmAmount = unconfirmAmount;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }
}
