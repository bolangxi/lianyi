package com.ted.resonance.service;

import com.ted.resonance.entity.Coupon;
import com.ted.resonance.entity.Transaction;
import com.ted.resonance.entity.valueobject.Nonce;
import com.ted.resonance.entity.valueobject.TransactionParams;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface TransactionService {
    /**
     * 发起共振，若有优惠券生成则返回优惠券， 否则返回null
     * @param params
     * @return
     */
    Coupon sendResonance(TransactionParams params, Integer phase) throws Exception;

    /**
     * 获取nonce
     */
    Nonce getNonce(String address) throws Exception;
    void transactionSuccess(Transaction transaction, Integer blockNumber);
    void transactionFail(Transaction transaction, Integer blockNumber);
    void rewardTransactionFail(Transaction transaction);
    void save(Transaction transaction);
    int getPhase();

    BigInteger getEtcBalance(String address) throws Exception;
    BigInteger getEthBalance(String address) throws Exception;

    BigDecimal getEtcReward(BigDecimal payment) throws Exception;
    BigDecimal getEthReward(BigDecimal payment) throws Exception;

    BigInteger getEtcStartBlock() throws Exception;
    BigInteger getEtcEndBlock() throws Exception;

    BigInteger getEthStartBlock() throws Exception;
    BigInteger getEthEndBlock() throws Exception;

    BigInteger getEtcCurrentBlock() throws Exception;
    BigInteger getEthCurrentBlock() throws Exception;
}
