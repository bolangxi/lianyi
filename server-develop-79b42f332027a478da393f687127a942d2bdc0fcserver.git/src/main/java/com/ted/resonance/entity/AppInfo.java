package com.ted.resonance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class AppInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "版本号", example = "1.0.0")
    String appVersion;
    @ApiModelProperty("新版app下载地址")
    String appUrl;
    @ApiModelProperty("更新日志")
    String appTips;
    @ApiModelProperty("系统 0 android 1 ios")
    Integer sys;
    @CreationTimestamp
    @JsonIgnore
    Date createdAt;
    @UpdateTimestamp
    @JsonIgnore
    Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppTips() {
        return appTips;
    }

    public void setAppTips(String appTips) {
        this.appTips = appTips;
    }

    public Integer getSys() {
        return sys;
    }

    public void setSys(Integer sys) {
        this.sys = sys;
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
}
