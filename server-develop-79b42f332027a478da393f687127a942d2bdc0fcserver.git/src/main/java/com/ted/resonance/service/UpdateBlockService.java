package com.ted.resonance.service;

import java.math.BigInteger;

/**
 * 这个服务主要用于定时去检查区块，遍历区块的最新交易， 当涉及到与TED 相关的交易的时候，更新数据库的状态
 */
public interface UpdateBlockService {
    void updateBlock();

    BigInteger getEtcNonce(String address) throws Exception;
    BigInteger getEthNonce(String address) throws Exception;
}
