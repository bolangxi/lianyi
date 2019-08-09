package com.ted.update.entity;

import java.util.Date;

public class Block {
    protected Long id;

    protected String hash;

    protected Integer height;

    protected Short state;

    protected String previous;

    protected Integer txCount;

    protected Integer size;

    protected Long timestamp;

    protected Integer synced;

    protected Date createTime;

    protected Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous == null ? null : previous.trim();
    }

    public Integer getTxCount() {
        return txCount;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSynced() {
        return synced;
    }

    public void setSynced(Integer synced) {
        this.synced = synced;
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
}