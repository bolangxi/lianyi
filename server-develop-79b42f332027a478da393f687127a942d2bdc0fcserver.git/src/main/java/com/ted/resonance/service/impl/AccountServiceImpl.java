package com.ted.resonance.service.impl;

import com.ted.resonance.entity.Account;
import com.ted.resonance.repository.AccountRepo;
import com.ted.resonance.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepo accountRepo;
    @Value("${fileServer}") private String fileServer;
    @Override
//    @Transactional
    public Account checkAccount(String address, String nation) {
        Account account = accountRepo.findByAddress(address);
        if(account == null) {
            Account newAccount = new Account();
            newAccount.setAddress(address);
            account = accountRepo.save(newAccount);
            //2019-7-3  昵称要改为id
            account.setNickname(account.getId().toString());
            account.setNation(nation);
            account = accountRepo.save(account);

        }
        if(account != null && account.getHeadPicture() == null) {
            //头像 随机
            int ranN = new Random().nextInt(30) + 128;
            account.setHeadPicture("head/"+ ranN + ".png");
            account = accountRepo.save(account);
        }
        if(account !=null && account.getNation() == null) {
            account.setNation("Other");
            account = accountRepo.save(account);
        }
        //文件服务器可能会变
        account.setHeadPicture(fileServer + account.getHeadPicture());
        return account;
    }

    @Override
    public Boolean isUsed(String nickname, String address) {
        if(accountRepo.findByAddressNotAndNickname(address, nickname) != null) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Account updateAccount(String nickname, String address) {
        if(isUsed(nickname, address)) {
            return null;
        }
        Account account = accountRepo.findByAddress(address);
        account.setNickname(nickname);
        return account;
    }

    @Override
    @Transactional
    public Account updateNation(String address, String nation) {
        Account account = accountRepo.findByAddress(address);
        if(account.getNation().equals(nation.toLowerCase())){
            return null;
        }
        account.setNation(nation.toLowerCase());
        return account;
    }
}
