package com.ted.update;

import static org.junit.Assert.assertTrue;

import com.ted.update.util.DateUtil;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;
import org.web3j.crypto.Hash;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
//        System.out.println("Integer.valueOf(\"0x1\".replace(\"0x\", \"\"), 16) = " + Integer.valueOf("0x1".replace("0x", ""), 16));
//        Calendar calendar = Calendar.getInstance();
//        System.out.println("calendar.getTime() = " + calendar.getTime());
//        System.out.println("DateUtil.initCurrentDate() = " + DateUtil.initCurrentDate());
        System.out.println("Hex.toHexString(Hash.sha3(\"burn(uint256)\".getBytes())) = " + Hex.toHexString(Hash.sha3("burn(uint256)".getBytes())));
        System.out.println("true = " + Long.parseLong("56bc75e2d63100000", 16));
    }

    public static String sha3256(byte[] bytes) {
        Digest digest = new SHA3Digest();
        digest.update(bytes, 0, bytes.length);
        byte[] rsData = new byte[digest.getDigestSize()];
        digest.doFinal(rsData, 0);
        return Hex.toHexString(rsData);
    }
}
