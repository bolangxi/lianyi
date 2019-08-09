package com.ted.resonance.service.impl;

import com.ted.resonance.entity.Coupon;
import com.ted.resonance.repository.CouponRepo;
import com.ted.resonance.service.CouponService;
import com.ted.resonance.utils.CouponGenerateStrategy;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private CouponRepo couponRepo;
    @Override
    @Transactional
    public Page<Coupon> getCouponByAddress(String address, int page, int pageSize) {
        List<Sort.Order> orders = new LinkedList<>();
        orders.add(Sort.Order.asc("status"));
        orders.add(Sort.Order.desc("createdAt"));
        Page<Coupon> coupons = couponRepo.findAllByAddress(address, PageRequest.of(page, pageSize, Sort.by(orders)));
        for(Coupon coupon : coupons) {
            //检查已使用的优惠券， 过期则更改状态
            if(coupon.getStatus() == 0) {
                if(coupon.getEndTime().getTime() < System.currentTimeMillis()) {
                    coupon.setStatus(2);
                    // 更改数据库状态
                    couponRepo.save(coupon);
                }
            }
        }
        return coupons;
    }

    @Override
    public int addCoupon(Coupon coupon) {
        couponRepo.save(coupon);
        return 0;
    }

    @Override
    public void startCoupon(Integer trasactionId, Integer blockNumber) {
        Coupon coupon = couponRepo.findByTransactionId(trasactionId);
        if(coupon == null) {
            return;
        }
        coupon.setStatus(0);  //有效
        coupon.setBeginTime(new Date());
        coupon.setEndTime(new Date(System.currentTimeMillis() + (long)coupon.getLeftTime()));
        coupon.setBeginBlock(blockNumber);
        coupon.setLeftBlock(5760); //24 * 60 * 60 /15
        coupon.setEndBlock(blockNumber + 5760);
        coupon = CouponGenerateStrategy.getSinature(coupon);
        couponRepo.save(coupon);
    }

    @Override
    public void recoverCoupon(Integer couponId, Integer blockNumber) {
        Coupon coupon = couponRepo.findById(couponId).get();
        coupon.setStatus(0);
        coupon.setEndBlock(coupon.getLeftBlock() + blockNumber);

        coupon.setEndTime(new Date(System.currentTimeMillis() + coupon.getLeftTime()));
        coupon = CouponGenerateStrategy.getSinature(coupon);
        couponRepo.save(coupon);
    }
}
