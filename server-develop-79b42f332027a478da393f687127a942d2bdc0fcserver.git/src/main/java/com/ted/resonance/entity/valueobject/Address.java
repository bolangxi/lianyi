package com.ted.resonance.entity.valueobject;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class Address {
    @ApiModelProperty(notes = "ETC/ETH 地址", example = "0x1234")
    @NotBlank
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
