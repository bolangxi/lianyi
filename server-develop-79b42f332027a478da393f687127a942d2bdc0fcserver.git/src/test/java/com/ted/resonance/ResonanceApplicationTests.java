package com.ted.resonance;

import com.ted.resonance.entity.*;
import com.ted.resonance.repository.AccountRepo;
import com.ted.resonance.repository.TeamRepo;
import com.ted.resonance.repository.TransactionRepo;
import com.ted.resonance.service.AddressInfoService;
import com.ted.resonance.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResonanceApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private CouponService couponService;

	@Test
	public void couponServiceTest() {
//		System.out.println(System.currentTimeMillis()/1000);
//		System.out.println(System.currentTimeMillis()/1000 - 24 * 60 * 60);
//		Coupon coupon = new Coupon();
//		coupon.setAddress("0x1234");
//		coupon.setDiscount(0.90f);
//		coupon.setPayment(10);
//		coupon.setReward(100f);
//		coupon.setStatus(0);
//		coupon.setType("etc");
//		coupon.setBeginTime(System.currentTimeMillis()/1000 - 24 * 60 * 60);
//		coupon.setEndTime(System.currentTimeMillis()/1000);
//		couponService.addCoupon(coupon);

		Page<Coupon> coupons = couponService.getCouponByAddress("0x1234", 0, 3);
		System.out.println(coupons.getContent());
		for(Coupon coupon : coupons.getContent()) {
			System.out.println(coupon.getStatus());
		}
	}

	@Autowired
	private AddressInfoService addressInfoService;

	@Test
	public void addressInfoServiceTest() {
//		AddressInfo info = addressInfoService.getAddressInfoById(2);
//		System.out.println(info.getAddress());
	}

	@Autowired
	private TransactionRepo transactionRepo;

	@Test
	public void transactionRepoTest() {
		transactionRepo.updateRewardStatus(1, 23);
	}

	@Autowired
	private AccountRepo accountRepo;
	@Test
	public void accountRepoTest() {
//		Account account = accountRepo.findByAddressNotAndNickname("0x123", "狗带");
//		Account account = accountRepo.findByNickname("狗带");
//		System.out.println(account.getNickname());
//		Account account = accountRepo.findByAddress("0x1234");
//		account.setNickname("狗带");
//		accountRepo.save(account);
//		System.out.println(account.getNickname());
	}

	@Autowired
	private TeamRepo teamRepo;

	@Test
	public void teamRepoTest() {

	}

}
