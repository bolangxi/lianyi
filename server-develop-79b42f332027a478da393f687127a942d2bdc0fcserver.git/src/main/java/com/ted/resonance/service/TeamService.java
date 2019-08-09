package com.ted.resonance.service;

import com.ted.resonance.entity.valueobject.TeamsInfo;

import java.math.BigDecimal;

public interface TeamService {
    //统计并返回团的信息
    TeamsInfo getTeamsInfo(String address, String type, Integer phase) throws Exception;

    //查询我的个人奖金并返回
    BigDecimal checkReward(String address, Integer phase, String type);

    //领取奖金
    void getReward(String address, Integer phase, String type, String signedString) throws Exception;
}
