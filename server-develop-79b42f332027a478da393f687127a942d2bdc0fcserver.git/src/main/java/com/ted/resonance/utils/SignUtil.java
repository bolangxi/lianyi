package com.ted.resonance.utils;

import com.ted.resonance.entity.response.WithdrawSignResponse;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SignUtil {
    public static WithdrawSignResponse signWithdraw(String privateKey, String toAddress, BigDecimal amount, Long id, int coinType) {
        Credentials workerCredentials = Credentials.create(privateKey);
        BigInteger value = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        // 这里按照合约的方法 传入参数顺序
        // 构造方法 第一个方法名， 第二个是合约入参列表， 第三个是合约返回参数列表
        Function params = new Function(
                "withdraw",
                Arrays.asList(
                        new Uint256(coinType),
                        new Uint256(id),
                        new Address(toAddress),
                        new Uint256(value)
                ),
                Arrays.asList(new TypeReference<Bool>() {
                })
        );
        StringBuilder sb = new StringBuilder();
        String str = encodeParameters(params.getInputParameters(), sb);
        Sign.SignatureData ss = Sign.signMessage(Numeric.hexStringToByteArray(str), workerCredentials.getEcKeyPair());
        //v
        System.out.println(ss.getV() & 0xFF);
        //r
        System.out.println(Numeric.toHexString(ss.getR()));
        //s
        System.out.println(ss.getS());
        WithdrawSignResponse withdrawSignResponse = new WithdrawSignResponse();
        withdrawSignResponse.setV(BigInteger.valueOf(ss.getV() & 0xFF));
        withdrawSignResponse.setR(Numeric.toHexString(ss.getR()));
        withdrawSignResponse.setS(Numeric.toHexString(ss.getS()));

        return withdrawSignResponse;
    }


    private static String encodeParameters(List<Type> parameters, StringBuilder result) {
        int dynamicDataOffset = getLength(parameters) * 32;
        StringBuilder dynamicData = new StringBuilder();
        Iterator var4 = parameters.iterator();

        while (var4.hasNext()) {
            Type parameter = (Type) var4.next();
            String encodedValue = TypeEncoder.encode(parameter);
            if (isDynamic(parameter)) {
                String encodedDataOffset = encodeNumeric(new Uint(BigInteger.valueOf((long) dynamicDataOffset)));
                result.append(encodedDataOffset);
                dynamicData.append(encodedValue);
                dynamicDataOffset += encodedValue.length() >> 1;
            } else {
                result.append(encodedValue);
            }
        }

        result.append(dynamicData);
        return result.toString();
    }

    static boolean isDynamic(Type parameter) {
        return parameter instanceof DynamicBytes || parameter instanceof Utf8String || parameter instanceof DynamicArray;
    }

    private static int getLength(List<Type> parameters) {
        int count = 0;
        Iterator var2 = parameters.iterator();

        while (var2.hasNext()) {
            Type type = (Type) var2.next();
            if (type instanceof StaticArray) {
                count += ((StaticArray) type).getValue().size();
            } else {
                ++count;
            }
        }

        return count;
    }

    static String encodeAddress(Address address) {
        return encodeNumeric(address.toUint160());
    }

    static String encodeNumeric(NumericType numericType) {
        byte[] rawValue = toByteArray(numericType);
        byte paddingValue = getPaddingValue(numericType);
        byte[] paddedRawValue = new byte[32];
        if (paddingValue != 0) {
            for (int i = 0; i < paddedRawValue.length; ++i) {
                paddedRawValue[i] = paddingValue;
            }
        }

        System.arraycopy(rawValue, 0, paddedRawValue, 32 - rawValue.length, rawValue.length);
        return Numeric.toHexStringNoPrefix(paddedRawValue);
    }

    private static byte getPaddingValue(NumericType numericType) {
        return (byte) (numericType.getValue().signum() == -1 ? -1 : 0);
    }

    private static byte[] toByteArray(NumericType numericType) {
        BigInteger value = numericType.getValue();
        if ((numericType instanceof Ufixed || numericType instanceof Uint) && value.bitLength() == 256) {
            byte[] byteArray = new byte[32];
            System.arraycopy(value.toByteArray(), 1, byteArray, 0, 32);
            return byteArray;
        } else {
            return value.toByteArray();
        }
    }

    public static void main(String[] args) throws Exception {
        SignUtil.signWithdraw("0xc8e8142c213d1f71a4af88f8fbb55267a52aca1f9e4d3ee4c822e1c305e53861",
                "0x05251255c1dc59347ce576C86a7BB989d33c00B7", new BigDecimal("10"), 1L, 0);
    }
}

//0xa6db8cc18e5d7518cd92fec6d4d14c741f01a5087f337ce6d28a5eeacb0f9176
//[B@6ebc05a6