package com.ted.resonance.service.impl;

import com.ted.resonance.entity.TokenBalance;
import com.ted.resonance.mapper.TokenBalanceBeanMapper;
import com.ted.resonance.service.TokenBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
public class TokenBalanceServiceImpl implements TokenBalanceService {
    @Autowired
    private TokenBalanceBeanMapper tokenBalanceRepo;

    @Override
    public BigDecimal findAllByAddr(String addr) {
        if(StringUtils.isEmpty(addr)){
            //addr 参数为空
            return null;

        }
        return tokenBalanceRepo.findAllByAddr(addr);
    }
}
