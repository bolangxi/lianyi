package com.ted.resonance.service;

import com.ted.resonance.entity.Coupon;
import org.springframework.data.domain.Page;

public interface CouponService {
    //分页获取优惠券
    Page<Coupon> getCouponByAddress(String address, int page, int PageSize);
    //添加优惠券
    int addCoupon(Coupon coupon);
    //优惠券生效
    void startCoupon(Integer transactionId, Integer blockNumber);
    //恢复优惠券
    void recoverCoupon(Integer transactionId, Integer blockNumber);
}
