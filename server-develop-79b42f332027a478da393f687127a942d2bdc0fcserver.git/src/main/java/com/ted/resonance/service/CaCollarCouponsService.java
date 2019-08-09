package com.ted.resonance.service;

import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.request.BalanceRequest;
import com.ted.resonance.entity.request.PayCouponsRequest;
import com.ted.resonance.entity.response.BalanceResponse;
import com.ted.resonance.utils.web.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CaCollarCouponsService {

    /**
     * 根据用户信息和领金券ID更新领金券状态
     *
     * @param caCollarCoupons
     * @return
     */
    int updateCollarCoupons(CaCollarCouponsBean caCollarCoupons);

    /**
     * 根据用户ID查询用户得领金券信息
     *
     * @param userId
     * @return
     */
    CaCollarCouponsBean findCaCollarCouponsByUserId(String userId);

    /**
     * 根据地址获取用户的领金券列表信息
     *
     * @param addr
     * @return
     */
    ResponseEntity<List<CaCollarCouponsBean>> couponsList(String addr);

    /**
     * 领金券支付  根据领金券Id支付该领金券，并更改改领金券状态为使用中
     *
     * @param payCouponsRequest
     * @return
     */
    ResponseEntity<CaCollarCouponsBean> payCoupons(PayCouponsRequest payCouponsRequest);

    /**
     * 计算炼金师收益入账
     *
     * @return
     */
    void calCouponIncome();

    /**
     * 收获(支持单个币种查询)
     *
     * @param balanceRequest
     * @return
     */
    List<BalanceResponse> selectBalances(BalanceRequest balanceRequest);

    /**
     * 获取用户可提现金额和提现手续费
     *
     * @param balanceRequest
     * @return
     */
    Map<String, Object> withDrawBalance(BalanceRequest balanceRequest);
}
