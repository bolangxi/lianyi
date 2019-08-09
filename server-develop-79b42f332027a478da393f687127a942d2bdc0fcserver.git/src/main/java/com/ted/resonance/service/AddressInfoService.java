package com.ted.resonance.service;

import com.ted.resonance.entity.AddressInfo;

import java.math.BigDecimal;

public interface AddressInfoService {
    //用于检查是否可填写邀请人
    Boolean inviterWritable(String address);
    //用于返回地址相关信息
    AddressInfo getAddressInfoById(int id);
    //保存地址信息
    void save(AddressInfo addressInfo);
    //根据地址和阶段返回地址信息
    AddressInfo getByAddressAndPhase(String address, Integer phase);

    //获取我的共振奖金
    BigDecimal getMyLeaderFund(String address, String type, Integer phase);
}
