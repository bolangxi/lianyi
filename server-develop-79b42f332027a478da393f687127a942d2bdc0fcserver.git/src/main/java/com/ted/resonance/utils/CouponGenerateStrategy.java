package com.ted.resonance.utils;

import com.ted.resonance.entity.Coupon;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Int8;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 优惠券生成策略
 */
public class CouponGenerateStrategy {
    public static Coupon generateCoupon(String type, BigDecimal payment) {
        if(type.toLowerCase().equals("eth")) {
            return generateEtcCoupon(payment);
        }else if(type.toLowerCase().equals("etc")) {
            return generateEthCoupon(payment);
        }
        return null;
    }

    private static Coupon generateEtcCoupon(BigDecimal payment) {
        //共振 eth  生成 etc 优惠券
        Coupon coupon = new Coupon();
        coupon.setType("etc");
        if(payment.compareTo(new BigDecimal("5")) < 0) {
            return null;
        }else if(payment.compareTo(new BigDecimal("10")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(45));
            coupon.setReward(BigDecimal.valueOf(16000));
            coupon.setDiscount(0.95f);
        }else if(payment.compareTo(new BigDecimal("20")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(90));
            coupon.setReward(BigDecimal.valueOf(33750));
            coupon.setDiscount(0.9f);
        }else if(payment.compareTo(new BigDecimal("30")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(155));
            coupon.setReward(BigDecimal.valueOf(61500));
            coupon.setDiscount(0.85f);
        }else if(payment.compareTo(new BigDecimal("40")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(220));
            coupon.setReward(BigDecimal.valueOf(92800));
            coupon.setDiscount(0.8f);
        }else if(payment.compareTo(new BigDecimal("50")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(330));
            coupon.setReward(BigDecimal.valueOf(150000));
            coupon.setDiscount(0.75f);
        }else if(payment.compareTo(new BigDecimal("60")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(450));
            coupon.setReward(BigDecimal.valueOf(217000));
            coupon.setDiscount(0.7f);
        }else if(payment.compareTo(new BigDecimal("70")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(580));
            coupon.setReward(BigDecimal.valueOf(300000));
            coupon.setDiscount(0.65f);
        }else if(payment.compareTo(new BigDecimal("80")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(710));
            coupon.setReward(BigDecimal.valueOf(400000));
            coupon.setDiscount(0.6f);
        }else if(payment.compareTo(new BigDecimal("100")) < 0) {
            coupon.setPayment(BigDecimal.valueOf(850));
            coupon.setReward(BigDecimal.valueOf(521000));
            coupon.setDiscount(0.55f);
        }else {
            //payment >= 100
            coupon.setPayment(BigDecimal.valueOf(1000));
            coupon.setReward(BigDecimal.valueOf(670000));
            coupon.setDiscount(0.5f);
        }
        return coupon;
    }

    private static Coupon generateEthCoupon(BigDecimal payment) {
        Coupon coupon = new Coupon();
        coupon.setType("eth");
        if(payment.compareTo(BigDecimal.valueOf(225)) < 0) {
            return null;
        }else if(payment.compareTo(BigDecimal.valueOf(450)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(1));
            coupon.setReward(BigDecimal.valueOf(15800));
            coupon.setDiscount(0.95f);
        }else if(payment.compareTo(BigDecimal.valueOf(900)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(2));
            coupon.setReward(BigDecimal.valueOf(33300));
            coupon.setDiscount(0.9f);
        }else if(payment.compareTo(BigDecimal.valueOf(1350)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(3.5));
            coupon.setReward(BigDecimal.valueOf(61800));
            coupon.setDiscount(0.85f);
        }else if(payment.compareTo(BigDecimal.valueOf(1800)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(5));
            coupon.setReward(BigDecimal.valueOf(93800));
            coupon.setDiscount(0.8f);
        }else if(payment.compareTo(BigDecimal.valueOf(2250)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(7.5));
            coupon.setReward(BigDecimal.valueOf(150000));
            coupon.setDiscount(0.75f);
        }else if(payment.compareTo(BigDecimal.valueOf(2700)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(10));
            coupon.setReward(BigDecimal.valueOf(215000));
            coupon.setDiscount(0.7f);
        }else if(payment.compareTo(BigDecimal.valueOf(3150)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(13));
            coupon.setReward(BigDecimal.valueOf(300000));
            coupon.setDiscount(0.65f);
        }else if(payment.compareTo(BigDecimal.valueOf(3600)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(16));
            coupon.setReward(BigDecimal.valueOf(400000));
            coupon.setDiscount(0.6f);
        }else if(payment.compareTo(BigDecimal.valueOf(4500)) < 0) {
            coupon.setPayment(BigDecimal.valueOf(19));
            coupon.setReward(BigDecimal.valueOf(520000));
            coupon.setDiscount(0.55f);
        }else {
            //payment >= 100
            coupon.setPayment(BigDecimal.valueOf(22));
            coupon.setReward(BigDecimal.valueOf(660000));
            coupon.setDiscount(0.5f);
        }
        return coupon;
    }

    public static Coupon getSinature(Coupon coupon) {
        Integer id = coupon.getId();
        String addr = coupon.getAddress();
        int typ = 0;
        if(coupon.getType().toLowerCase().equals("eth")) {
            typ = 1;
        }
        BigInteger etc = Convert.toWei(coupon.getPayment(), Convert.Unit.ETHER).toBigInteger();
        BigInteger ted = coupon.getReward().toBigInteger().multiply(BigDecimal.valueOf(Math.pow(10,18)).toBigInteger());
        Integer blk = coupon.getEndBlock();
        Function useCoupon = new Function(
                "useCoupon",
                Arrays.asList(new Uint256(id),
                        new Address(addr),
                        new Uint8(typ),
                        new Uint256(etc),
                        new Uint256(ted),
                        new Uint256(blk)),
                Arrays.asList(new TypeReference<Bool>() {})
        );
//        String str = FunctionEncoder.encode(useCoupon);
//        Credentials credentials = Credentials.create("0xf274ea29e795ad08cd614493804cbe1f85b868e75825b0267f57adcda089eb91");
        StringBuilder sb = new StringBuilder();
        String str = ContractUtils.encodeParameters(useCoupon.getInputParameters(), sb);
        Credentials credentials =Credentials.create("0xc8e8142c213d1f71a4af88f8fbb55267a52aca1f9e4d3ee4c822e1c305e53861");
        Sign.SignatureData signatureData = Sign.signMessage(Numeric.hexStringToByteArray(str), credentials.getEcKeyPair());
        coupon.setR(toHexString(signatureData.getR()));
        coupon.setS(toHexString(signatureData.getS()));
        int v = signatureData.getV();
        coupon.setV(String.valueOf(v));
        return coupon;
    }


    public static String toHexString(byte[] byteArray) {
        final StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0)
            return null;
        for (int i = 0; i < byteArray.length; i++) {
            int v = byteArray[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }


}
