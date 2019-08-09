package com.ted.resonance.utils;

import org.web3j.protocol.Web3j;

import java.math.BigInteger;

/*
缓存一些不常改变的不重要变量
 */
public class CacheUtils {
    private static BigInteger etcStartBlock;
    private static BigInteger etcEndBlock;
    private static BigInteger ethStartBlock;
    private static BigInteger ethEndBlock;

    public static BigInteger getEtcStartBlock() {
        return etcStartBlock;
    }

    public static void setEtcStartBlock(BigInteger etcStartBlock) {
        CacheUtils.etcStartBlock = etcStartBlock;
    }

    public static BigInteger getEtcEndBlock() {
        return etcEndBlock;
    }

    public static void setEtcEndBlock(BigInteger etcEndBlock) {
        CacheUtils.etcEndBlock = etcEndBlock;
    }

    public static BigInteger getEthStartBlock() {
        return ethStartBlock;
    }

    public static void setEthStartBlock(BigInteger ethStartBlock) {
        CacheUtils.ethStartBlock = ethStartBlock;
    }

    public static BigInteger getEthEndBlock() {
        return ethEndBlock;
    }

    public static void setEthEndBlock(BigInteger ethEndBlock) {
        CacheUtils.ethEndBlock = ethEndBlock;
    }
}
