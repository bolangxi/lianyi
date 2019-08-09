package com.ted.resonance.entity;

import java.math.BigDecimal;
import java.util.Date;

public class TxBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.hash
     *
     * @mbggenerated
     */
    private String hash;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.from_addr
     *
     * @mbggenerated
     */
    private String fromAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.to_addr
     *
     * @mbggenerated
     */
    private String toAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.fee
     *
     * @mbggenerated
     */
    private BigDecimal fee;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.eth_amount
     *
     * @mbggenerated
     */
    private BigDecimal ethAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.block_hash
     *
     * @mbggenerated
     */
    private String blockHash;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.block_height
     *
     * @mbggenerated
     */
    private Integer blockHeight;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.block_state
     *
     * @mbggenerated
     */
    private Short blockState;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.timestamp
     *
     * @mbggenerated
     */
    private Long timestamp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.state
     *
     * @mbggenerated
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.asset_short_name
     *
     * @mbggenerated
     */
    private String assetShortName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.confirm
     *
     * @mbggenerated
     */
    private Short confirm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.gas_limit
     *
     * @mbggenerated
     */
    private BigDecimal gasLimit;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.gas_price
     *
     * @mbggenerated
     */
    private BigDecimal gasPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.tx_id
     *
     * @mbggenerated
     */
    private String txId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.tx_num
     *
     * @mbggenerated
     */
    private Integer txNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.comment
     *
     * @mbggenerated
     */
    private String comment;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.nonce
     *
     * @mbggenerated
     */
    private Integer nonce;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.main_coin
     *
     * @mbggenerated
     */
    private String mainCoin;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tx.ted_amount
     *
     * @mbggenerated
     */
    private BigDecimal tedAmount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.id
     *
     * @return the value of tx.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.id
     *
     * @param id the value for tx.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.hash
     *
     * @return the value of tx.hash
     *
     * @mbggenerated
     */
    public String getHash() {
        return hash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.hash
     *
     * @param hash the value for tx.hash
     *
     * @mbggenerated
     */
    public void setHash(String hash) {
        this.hash = hash == null ? null : hash.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.from_addr
     *
     * @return the value of tx.from_addr
     *
     * @mbggenerated
     */
    public String getFromAddr() {
        return fromAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.from_addr
     *
     * @param fromAddr the value for tx.from_addr
     *
     * @mbggenerated
     */
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr == null ? null : fromAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.to_addr
     *
     * @return the value of tx.to_addr
     *
     * @mbggenerated
     */
    public String getToAddr() {
        return toAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.to_addr
     *
     * @param toAddr the value for tx.to_addr
     *
     * @mbggenerated
     */
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr == null ? null : toAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.fee
     *
     * @return the value of tx.fee
     *
     * @mbggenerated
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.fee
     *
     * @param fee the value for tx.fee
     *
     * @mbggenerated
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.eth_amount
     *
     * @return the value of tx.eth_amount
     *
     * @mbggenerated
     */
    public BigDecimal getEthAmount() {
        return ethAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.eth_amount
     *
     * @param ethAmount the value for tx.eth_amount
     *
     * @mbggenerated
     */
    public void setEthAmount(BigDecimal ethAmount) {
        this.ethAmount = ethAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.block_hash
     *
     * @return the value of tx.block_hash
     *
     * @mbggenerated
     */
    public String getBlockHash() {
        return blockHash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.block_hash
     *
     * @param blockHash the value for tx.block_hash
     *
     * @mbggenerated
     */
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash == null ? null : blockHash.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.block_height
     *
     * @return the value of tx.block_height
     *
     * @mbggenerated
     */
    public Integer getBlockHeight() {
        return blockHeight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.block_height
     *
     * @param blockHeight the value for tx.block_height
     *
     * @mbggenerated
     */
    public void setBlockHeight(Integer blockHeight) {
        this.blockHeight = blockHeight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.block_state
     *
     * @return the value of tx.block_state
     *
     * @mbggenerated
     */
    public Short getBlockState() {
        return blockState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.block_state
     *
     * @param blockState the value for tx.block_state
     *
     * @mbggenerated
     */
    public void setBlockState(Short blockState) {
        this.blockState = blockState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.timestamp
     *
     * @return the value of tx.timestamp
     *
     * @mbggenerated
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.timestamp
     *
     * @param timestamp the value for tx.timestamp
     *
     * @mbggenerated
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.create_time
     *
     * @return the value of tx.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.create_time
     *
     * @param createTime the value for tx.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.update_time
     *
     * @return the value of tx.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.update_time
     *
     * @param updateTime the value for tx.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.state
     *
     * @return the value of tx.state
     *
     * @mbggenerated
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.state
     *
     * @param state the value for tx.state
     *
     * @mbggenerated
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.asset_short_name
     *
     * @return the value of tx.asset_short_name
     *
     * @mbggenerated
     */
    public String getAssetShortName() {
        return assetShortName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.asset_short_name
     *
     * @param assetShortName the value for tx.asset_short_name
     *
     * @mbggenerated
     */
    public void setAssetShortName(String assetShortName) {
        this.assetShortName = assetShortName == null ? null : assetShortName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.confirm
     *
     * @return the value of tx.confirm
     *
     * @mbggenerated
     */
    public Short getConfirm() {
        return confirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.confirm
     *
     * @param confirm the value for tx.confirm
     *
     * @mbggenerated
     */
    public void setConfirm(Short confirm) {
        this.confirm = confirm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.gas_limit
     *
     * @return the value of tx.gas_limit
     *
     * @mbggenerated
     */
    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.gas_limit
     *
     * @param gasLimit the value for tx.gas_limit
     *
     * @mbggenerated
     */
    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.gas_price
     *
     * @return the value of tx.gas_price
     *
     * @mbggenerated
     */
    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.gas_price
     *
     * @param gasPrice the value for tx.gas_price
     *
     * @mbggenerated
     */
    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.tx_id
     *
     * @return the value of tx.tx_id
     *
     * @mbggenerated
     */
    public String getTxId() {
        return txId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.tx_id
     *
     * @param txId the value for tx.tx_id
     *
     * @mbggenerated
     */
    public void setTxId(String txId) {
        this.txId = txId == null ? null : txId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.type
     *
     * @return the value of tx.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.type
     *
     * @param type the value for tx.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.tx_num
     *
     * @return the value of tx.tx_num
     *
     * @mbggenerated
     */
    public Integer getTxNum() {
        return txNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.tx_num
     *
     * @param txNum the value for tx.tx_num
     *
     * @mbggenerated
     */
    public void setTxNum(Integer txNum) {
        this.txNum = txNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.comment
     *
     * @return the value of tx.comment
     *
     * @mbggenerated
     */
    public String getComment() {
        return comment;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.comment
     *
     * @param comment the value for tx.comment
     *
     * @mbggenerated
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.nonce
     *
     * @return the value of tx.nonce
     *
     * @mbggenerated
     */
    public Integer getNonce() {
        return nonce;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.nonce
     *
     * @param nonce the value for tx.nonce
     *
     * @mbggenerated
     */
    public void setNonce(Integer nonce) {
        this.nonce = nonce;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.main_coin
     *
     * @return the value of tx.main_coin
     *
     * @mbggenerated
     */
    public String getMainCoin() {
        return mainCoin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.main_coin
     *
     * @param mainCoin the value for tx.main_coin
     *
     * @mbggenerated
     */
    public void setMainCoin(String mainCoin) {
        this.mainCoin = mainCoin == null ? null : mainCoin.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tx.ted_amount
     *
     * @return the value of tx.ted_amount
     *
     * @mbggenerated
     */
    public BigDecimal getTedAmount() {
        return tedAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tx.ted_amount
     *
     * @param tedAmount the value for tx.ted_amount
     *
     * @mbggenerated
     */
    public void setTedAmount(BigDecimal tedAmount) {
        this.tedAmount = tedAmount;
    }
}