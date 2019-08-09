package com.ted.update.util;

import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by qingyun.yu on 2018/11/22.
 */
public class DataUtil {
    public static String generateIndexId(String... strs) {
        return Stream.of(strs).reduce((temp, item) -> temp + "_" + item).get();
    }

    public static List<Integer> generateLongList(Integer startLong, Integer endLong) {
        List<Integer> result = new ArrayList<>();
        for(Integer l = startLong; l < endLong;) {
            result.add(++l);
        }
        return result;
    }

    /**
     * 转账金额转换
     *
     * @param price
     * @param decimal
     * @return
     */
    public static BigDecimal formatAmount(BigDecimal price, Integer decimal) {
        BigDecimal coefficient = new BigDecimal(Math.pow(10, decimal));
        return price.divide(coefficient).setScale(8, BigDecimal.ROUND_DOWN);
    }

    /**
     * 16进制,转账金额转换bigdecimal
     *
     * @param priceHex16
     * @param decimal
     * @return
     */
    public static BigDecimal formatAmount(String priceHex16, Integer decimal) {
        BigInteger bigInteger = new BigInteger(priceHex16.substring(2), 16);
        BigDecimal bigDecimal = new BigDecimal(bigInteger);
        BigDecimal coefficient = new BigDecimal(Math.pow(10, decimal));
        return bigDecimal.divide(coefficient).setScale(8, BigDecimal.ROUND_DOWN);
    }

    public static String longToHex(Long value) {
        return "0x" + Long.toHexString(value);
    }

    public static Long hexToLong(String value) {
        return Long.parseLong(value.replace("0x", ""), 16);
    }

    public static byte[] toByteArray(String hexString) {
        if (Strings.isBlank(hexString)) {
            throw new IllegalArgumentException("this hexString must not be empty");
        }

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }
}
