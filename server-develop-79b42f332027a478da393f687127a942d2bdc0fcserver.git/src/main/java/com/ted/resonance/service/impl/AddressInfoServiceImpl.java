package com.ted.resonance.service.impl;

import com.ted.resonance.entity.AddressInfo;
import com.ted.resonance.repository.AddressInfoRepo;
import com.ted.resonance.service.AddressInfoService;
import com.ted.resonance.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AddressInfoServiceImpl implements AddressInfoService {
    @Autowired
    private AddressInfoRepo addressInfoRepo;
    @Autowired
    private TransactionService transactionService;

    @Override
    public Boolean inviterWritable(String address) {
        AddressInfo addressInfo = addressInfoRepo.findByAddressAndPhase(address, transactionService.getPhase());
        if(addressInfo == null) {
            return true;
        }
        //没有填写过邀请人
        if(addressInfo.getInviterAddress() == null || ("").equals(addressInfo.getInviterAddress())) {
            return true;
        }
        //入团失败 或者是邀请人 可重新入团
        if(addressInfo.getStatus() == 2) {
            return true;
        }
        return false;
    }

    @Override
    public AddressInfo getAddressInfoById(int id) {
        return addressInfoRepo.findById(id).get();
    }

    @Override
    public void save(AddressInfo addressInfo) {
        addressInfoRepo.save(addressInfo);
    }

    @Override
    public AddressInfo getByAddressAndPhase(String address, Integer phase) {
        return addressInfoRepo.findByAddressAndPhase(address, phase);
    }

    @Override
    public BigDecimal getMyLeaderFund(String address, String type, Integer phase) {
        BigDecimal fund = BigDecimal.valueOf(0);
        AddressInfo addressInfo = addressInfoRepo.findByAddressAndPhase(address, phase);
        fund = fund.add(calMyLeaderFund(addressInfo, type, phase));
        return fund;
    }

    private BigDecimal calMyLeaderFund(AddressInfo addressInfo, String type, Integer phase) {
        BigDecimal fund = BigDecimal.valueOf(0);
        if("etc".equals(type.toLowerCase()) && addressInfo.getEtcPayment() != null) {
            fund = fund.add(addressInfo.getEtcPayment());
        }
        if("eth".equals(type.toLowerCase()) && addressInfo.getEthPayment() != null) {
            fund = fund.add(addressInfo.getEthPayment());
        }
        List<AddressInfo> infos  = addressInfoRepo.findByInviterAddressAndPhase(addressInfo.getAddress(), phase);
        for(AddressInfo info : infos) {
            System.out.println(info.getAddress());
            fund = fund.add(calMyLeaderFund(info, type, phase));
        }
        return fund;
    }
}
