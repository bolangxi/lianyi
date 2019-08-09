package com.ted.resonance.service;

import com.ted.resonance.entity.TokenBalance;

import java.math.BigDecimal;

public interface TokenBalanceService {

    /**
     * 根据地址查询用户TED余额
     * @param addr
     * @return
     */
    BigDecimal findAllByAddr(String addr);
}
