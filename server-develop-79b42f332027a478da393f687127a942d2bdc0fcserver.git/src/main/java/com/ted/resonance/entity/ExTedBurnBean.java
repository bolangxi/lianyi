package com.ted.resonance.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ExTedBurnBean {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.id
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.activaty_id
     *
     * @mbggenerated
     */
    private Long activityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.user_id
     *
     * @mbggenerated
     */
    private Long userId;

    private String addr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.tx_hash
     *
     * @mbggenerated
     */
    private String txHash;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.block_height
     *
     * @mbggenerated
     */
    private Integer blockHeight;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.block_hash
     *
     * @mbggenerated
     */
    private String blockHash;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.amount
     *
     * @mbggenerated
     */
    private BigDecimal amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.contribution_degree
     *
     * @mbggenerated
     */
    private BigDecimal contributionDegree;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.status
     *
     * @mbggenerated
     */
    private Integer status;


    private Integer calStatus;



    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ex_ted_burn.is_deleted
     *
     * @mbggenerated
     */
    private Boolean isDeleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.id
     *
     * @return the value of ex_ted_burn.id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.id
     *
     * @param id the value for ex_ted_burn.id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.activaty_id
     *
     * @return the value of ex_ted_burn.activaty_id
     *
     * @mbggenerated
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.activaty_id
     *
     * @param activityId the value for ex_ted_burn.activaty_id
     *
     * @mbggenerated
     */
    public void setActivatyId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.user_id
     *
     * @return the value of ex_ted_burn.user_id
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.user_id
     *
     * @param userId the value for ex_ted_burn.user_id
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.tx_hash
     *
     * @return the value of ex_ted_burn.tx_hash
     *
     * @mbggenerated
     */
    public String getTxHash() {
        return txHash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.tx_hash
     *
     * @param txHash the value for ex_ted_burn.tx_hash
     *
     * @mbggenerated
     */
    public void setTxHash(String txHash) {
        this.txHash = txHash == null ? null : txHash.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.block_height
     *
     * @return the value of ex_ted_burn.block_height
     *
     * @mbggenerated
     */
    public Integer getBlockHeight() {
        return blockHeight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.block_height
     *
     * @param blockHeight the value for ex_ted_burn.block_height
     *
     * @mbggenerated
     */
    public void setBlockHeight(Integer  blockHeight) {
        this.blockHeight = blockHeight;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.block_hash
     *
     * @return the value of ex_ted_burn.block_hash
     *
     * @mbggenerated
     */
    public String getBlockHash() {
        return blockHash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.block_hash
     *
     * @param blockHash the value for ex_ted_burn.block_hash
     *
     * @mbggenerated
     */
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.amount
     *
     * @return the value of ex_ted_burn.amount
     *
     * @mbggenerated
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.amount
     *
     * @param amount the value for ex_ted_burn.amount
     *
     * @mbggenerated
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.contribution_degree
     *
     * @return the value of ex_ted_burn.contribution_degree
     *
     * @mbggenerated
     */
    public BigDecimal getContributionDegree() {
        return contributionDegree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.contribution_degree
     *
     * @param contributionDegree the value for ex_ted_burn.contribution_degree
     *
     * @mbggenerated
     */
    public void setContributionDegree(BigDecimal contributionDegree) {
        this.contributionDegree = contributionDegree;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.status
     *
     * @return the value of ex_ted_burn.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.status
     *
     * @param status the value for ex_ted_burn.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.create_time
     *
     * @return the value of ex_ted_burn.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.create_time
     *
     * @param createTime the value for ex_ted_burn.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.update_time
     *
     * @return the value of ex_ted_burn.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.update_time
     *
     * @param updateTime the value for ex_ted_burn.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column ex_ted_burn.is_deleted
     *
     * @return the value of ex_ted_burn.is_deleted
     *
     * @mbggenerated
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column ex_ted_burn.is_deleted
     *
     * @param isDeleted the value for ex_ted_burn.is_deleted
     *
     * @mbggenerated
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


    public Integer getCalStatus() {
        return calStatus;
    }

    public void setCalStatus(Integer calStatus) {
        this.calStatus = calStatus;
    }
}