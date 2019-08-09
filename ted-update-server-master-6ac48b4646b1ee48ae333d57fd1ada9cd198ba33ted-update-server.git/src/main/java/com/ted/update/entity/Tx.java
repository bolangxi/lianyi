package com.ted.update.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Tx {
    private Long id;

    private String hash;

    private String fromAddr;

    private String toAddr;

    private BigDecimal fee;

    private BigDecimal ethAmount;
    
    private BigDecimal tedAmount;

    private String blockHash;

    private Integer blockHeight;

    private Short blockState;

    private Long timestamp;

    private Date createTime;

    private Date updateTime;

    private Integer state;

    private String assetShortName;

    private Integer confirm;

    private BigDecimal gasLimit;

    private BigDecimal gasPrice;

    private String txId;

    private String type;

    private Integer txNum;

    private String comment;

    private Integer nonce;

    private String mainCoin;

    private String contractAddr;

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

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

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr == null ? null : fromAddr.trim();
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr == null ? null : toAddr.trim();
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getEthAmount() {
        return ethAmount;
    }

    public void setEthAmount(BigDecimal ethAmount) {
        this.ethAmount = ethAmount;
    }

    public BigDecimal getTedAmount() {
        return tedAmount;
    }

    public void setTedAmount(BigDecimal tedAmount) {
        this.tedAmount = tedAmount;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
    }

    public Integer getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    public Short getBlockState() {
        return blockState;
    }

    public void setBlockState(Short blockState) {
        this.blockState = blockState;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAssetShortName() {
        return assetShortName;
    }

    public void setAssetShortName(String assetShortName) {
        this.assetShortName = assetShortName == null ? null : assetShortName.trim();
    }

    public Integer getConfirm() {
        return confirm;
    }

    public void setConfirm(Integer confirm) {
        this.confirm = confirm;
    }

    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId == null ? null : txId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getTxNum() {
        return txNum;
    }

    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getNonce() {
        return nonce;
    }

    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    public String getMainCoin() {
        return mainCoin;
    }

    public void setMainCoin(String mainCoin) {
        this.mainCoin = mainCoin;
    }
}