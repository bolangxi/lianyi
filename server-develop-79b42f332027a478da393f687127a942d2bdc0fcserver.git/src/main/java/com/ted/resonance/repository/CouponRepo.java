package com.ted.resonance.repository;

import com.ted.resonance.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepo extends JpaRepository<Coupon, Integer> {
    Page<Coupon> findAllByAddress(String address, Pageable pageable);
    Coupon findByTransactionId(Integer id);
}
