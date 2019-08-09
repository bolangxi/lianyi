package com.ted.resonance.service;

import com.ted.resonance.entity.Account;

public interface AccountService {
    //账户信息不存在会创建一个并返回
    Account checkAccount(String address, String nation);

    //检查昵称是否可用
    Boolean isUsed(String nickname, String address);

    //更新昵称
    Account updateAccount(String nickname, String address);

    //更改国家
    Account updateNation(String address, String nation);
}
