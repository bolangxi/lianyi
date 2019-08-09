package com.ted.resonance.service;

import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.CaDrawChanceBean;
import com.ted.resonance.utils.web.ResponseEntity;

public interface CaDrawChanceService {

    /**
     * 根据地址查询用户的当前可以抽奖的情况
     * @param addr
     * @return
     */
    CaDrawChanceBean findCaDrawChanceByAddr(String addr);

    /**
     * 根据主键ID更新领金券状态
     * @param id
     * @return
     */
    ResponseEntity<CaCollarCouponsBean> updateByCouponId(Long id, String addr);


}
