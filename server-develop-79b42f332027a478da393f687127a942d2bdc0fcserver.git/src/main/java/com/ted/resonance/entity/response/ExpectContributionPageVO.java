package com.ted.resonance.entity.response;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class ExpectContributionPageVO {

    @ApiModelProperty(notes = "预计可获得贡献值")
    private BigDecimal expectContribution;

    public BigDecimal getExpectContribution() {
        return expectContribution;
    }

    public void setExpectContribution(BigDecimal expectContribution) {
        this.expectContribution = expectContribution;
    }
}
