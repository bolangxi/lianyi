package com.ted.update.service;

import com.ted.update.entity.Account;

import java.util.List;

public interface AccountService {
    List<Account> selectAccountByAddress(String fromAddr, String toAddr);
}
