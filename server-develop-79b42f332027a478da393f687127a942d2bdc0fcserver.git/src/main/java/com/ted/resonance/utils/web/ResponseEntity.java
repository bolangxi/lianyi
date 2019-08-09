package com.ted.resonance.utils.web;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResponseEntity<T> implements Serializable {
    @ApiModelProperty(value = "状态", example = "200")
    private Integer status;
    @ApiModelProperty(example = "success")
    private String message;
    private T content;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
