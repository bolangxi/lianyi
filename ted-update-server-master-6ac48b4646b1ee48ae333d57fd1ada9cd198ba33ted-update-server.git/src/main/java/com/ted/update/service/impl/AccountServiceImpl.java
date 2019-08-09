package com.ted.update.service.impl;

import com.ted.update.dao.AccountDao;
import com.ted.update.entity.Account;
import com.ted.update.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    @Override
    public List<Account> selectAccountByAddress(String fromAddr, String toAddr) {
        return accountDao.selectAccountByAddress(fromAddr, toAddr);
    }
}
