package com.ted.resonance.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.models.auth.In;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class AddressInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address;
    private Integer phase;
    private String inviterAddress;
    private Integer status;
    private Integer teamId;
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createdAt;
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date updatedAt;
    @Column(columnDefinition = "decimal(30,18)")
    private BigDecimal ethPayment;
    @Column(columnDefinition = "decimal(30,18)")
    private BigDecimal etcPayment;

    private Boolean etcRewarded;
    private Boolean ethRewarded;

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

    public Integer getPhase() {
        return phase;
    }

    public void setPhase(Integer phase) {
        this.phase = phase;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getInviterAddress() {
        return inviterAddress;
    }

    public void setInviterAddress(String inviterAddress) {
        this.inviterAddress = inviterAddress;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public BigDecimal getEthPayment() {
        return ethPayment;
    }

    public void setEthPayment(BigDecimal ethPayment) {
        this.ethPayment = ethPayment;
    }

    public BigDecimal getEtcPayment() {
        return etcPayment;
    }

    public void setEtcPayment(BigDecimal etcPayment) {
        this.etcPayment = etcPayment;
    }

    public Boolean getEtcRewarded() {
        return etcRewarded;
    }

    public void setEtcRewarded(Boolean etcRewarded) {
        this.etcRewarded = etcRewarded;
    }

    public Boolean getEthRewarded() {
        return ethRewarded;
    }

    public void setEthRewarded(Boolean ethRewarded) {
        this.ethRewarded = ethRewarded;
    }
}
