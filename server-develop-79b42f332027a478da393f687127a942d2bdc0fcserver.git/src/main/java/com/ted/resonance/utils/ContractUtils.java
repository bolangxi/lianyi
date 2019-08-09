package com.ted.resonance.utils;

import com.ted.resonance.utils.exceptions.CommonException;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.*;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

public class ContractUtils {
    /**
     * 获取汇率
     * @param workerAddress
     * @param contractAddress
     * @param client
     * @return
     * @throws Exception
     */

    public static BigInteger getRatio(String workerAddress, String contractAddress, Web3j client) throws Exception{
        Function ratio = new Function(
                "ratio",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Int>() {})
        );
        String encodedRatio = FunctionEncoder.encode(ratio);

        EthCall ethCall = client.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(workerAddress, contractAddress, encodedRatio), DefaultBlockParameterName.LATEST).send();
        List<Type> someTypes = FunctionReturnDecoder.decode(ethCall.getValue(), ratio.getOutputParameters());

        Int number = (Int)someTypes.get(0);
        return number.getValue();
    }

    public static BigInteger calReward(String workerAddress, BigDecimal payment, String type, String contractAddress, Web3j client) throws Exception{
//        String methodName = "etc".equals(type) ? "ratioWithETC" : "ratioWithETH";
        Function reward = new Function(
                "ratioWithCoin",
                Arrays.asList(new Uint256(payment.movePointRight(18).unscaledValue())),
                Arrays.asList(new TypeReference<Uint256>() {})
        );
        String encodedRatio = FunctionEncoder.encode(reward);

        EthCall ethCall = client.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(workerAddress, contractAddress, encodedRatio), DefaultBlockParameterName.LATEST).send();
        List<Type> someTypes = FunctionReturnDecoder.decode(ethCall.getValue(), reward.getOutputParameters());
        Uint256 number = (Uint256) someTypes.get(0);
        return number.getValue();
    }
    /**
     * 获取期数
     * @deprecated
     * @param workerAddress
     * @param contractAddress
     * @param client
     * @return
     */
    public static BigInteger getPeriod(String workerAddress, String contractAddress, Web3j client) throws Exception {
        List returParams = Arrays.asList(new TypeReference<Int256>() {});
        List<Type> someTypes = getVariable(workerAddress, contractAddress, client, "period", returParams);

        Int256 endBlock = (Int256)someTypes.get(0);
        return endBlock.getValue();
    }

    public static BigInteger getStartBlock(String workerAddress, String contractAddress, Web3j client) throws Exception {
        List returParams = Arrays.asList(new TypeReference<Int256>() {});
        List<Type> someTypes = getVariable(workerAddress, contractAddress, client, "startBlock", returParams);

        Int256 endBlock = (Int256)someTypes.get(0);
        return endBlock.getValue();
    }

    public static BigInteger getEndBlock(String workerAddress, String contractAddress, Web3j client) throws Exception {
        List returParams = Arrays.asList(new TypeReference<Int256>() {});
        List<Type> someTypes = getVariable(workerAddress, contractAddress, client, "endBlock", returParams);

        Int256 endBlock = (Int256)someTypes.get(0);
        return endBlock.getValue();
    }

    public static BigInteger getCurrentStage(String workerAddress, String contractAddress, Web3j client) throws Exception {
        List returParams = Arrays.asList(new TypeReference<Uint256>() {});
        List<Type> someTypes = getVariable(workerAddress, contractAddress, client, "stageNumber", returParams);

        Uint256 endBlock = (Uint256)someTypes.get(0);
        return endBlock.getValue();
    }

    private static List<Type> getVariable(String workerAddress, String contractAddress, Web3j client, String varName, List paramType) throws Exception{
        Function function = new Function(
                varName,
                Arrays.asList(),
                paramType
        );
        String encodedFunction = FunctionEncoder.encode(function);

        EthCall ethCall = client.ethCall(org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(workerAddress, contractAddress, encodedFunction), DefaultBlockParameterName.LATEST).send();
        List<Type> someTypes = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
        return someTypes;
    }


    public static String ethExchange(Web3j client, String contractAddress, Credentials workerCredentials,
                            String toAddress, String txHash, BigInteger eth, BigInteger ted) throws Exception {
        Bytes32 hsh = new Bytes32(Numeric.hexStringToByteArray(txHash));
        Address address = new Address(toAddress);
        Uint256 ethP = new Uint256(eth);
        Uint256 tedP = new Uint256(ted);
        Function function = new Function(
                "ethExchange",  // function we're calling
                Arrays.asList(hsh, address, ethP, tedP),  // Parameters to pass as Solidity Type
                Arrays.asList(new TypeReference<Bool>(){})
        );
        BigInteger value = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();

        BigInteger nonce = client.ethGetTransactionCount(workerCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction contractTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, contractAddress, value,encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(contractTransaction, workerCredentials);
        String hexString = Numeric.toHexString(signedMessage);
        EthSendTransaction sendTransaction = client.ethSendRawTransaction(hexString).send();
        if(sendTransaction.getTransactionHash() == null){
            return sendTransaction.getError().getMessage();
        }
        return sendTransaction.getTransactionHash();
    }

    public static Sign.SignatureData getSignature(int period, String self, BigInteger reward, Credentials workerCredentials) throws Exception{
        if(period < 1 || period > 3) {
            throw new CommonException(999, "phase not legal");
        }
        period = period -1;
        Function params = new Function(
                "knightWithdraw",
                Arrays.asList(new Uint8(period),  //期数
                        new Address(self),   //领奖者
                        new Uint256(reward)  //总etc数量
                ),
                Arrays.asList(new TypeReference<Bool>() {})
        );

        StringBuilder sb = new StringBuilder();
        String str = encodeParameters(params.getInputParameters(), sb);
        Sign.SignatureData signatureData = Sign.signMessage(Numeric.hexStringToByteArray(str), workerCredentials.getEcKeyPair());
        return signatureData;
    }

    /**
     * 骑士团领奖
     * @param client
     * @param contractAddress
     * @param workerCredentials
     * @param period
     * @param self
     * @return
     */

    public static String knightWithdraw(Web3j client, String contractAddress, Credentials workerCredentials,
                                       int period, String self, BigInteger reward
                                       ) throws Exception {
//        if(period < 1 || period > 3) {
//            throw new CommonException(999, "phase not legal");
//        }
//        period = period -1;
//        Function params = new Function(
//                "knightWithdraw",
//                Arrays.asList(new Uint8(period),  //期数
//                        new Address(self),   //领奖者
//                        new Uint256(reward)  //总etc数量
//                        ),
//                Arrays.asList(new TypeReference<Bool>() {})
//        );
//
//        StringBuilder sb = new StringBuilder();
//        String str = encodeParameters(params.getInputParameters(), sb);
//        Sign.SignatureData signatureData = Sign.signMessage(Numeric.hexStringToByteArray(str), workerCredentials.getEcKeyPair());

//        Function knightWithdraw = new Function(
//                "knightWithdraw",
//                Arrays.asList(new Uint8(period),  //期数
//                        new Address(self),   //领奖者
//                        new Uint256(reward),  //总etc数量
//                        new Uint8(signatureData.getV()),
//                        new Bytes32(signatureData.getR()),
//                        new Bytes32(signatureData.getS())),
//                Arrays.asList(new TypeReference<Bool>() {})
//        );
//
//        BigInteger value = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();
//
//        BigInteger nonce = client.ethGetTransactionCount(workerCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount();
//        String encodedFunction = FunctionEncoder.encode(knightWithdraw);
//        RawTransaction contractTransaction = RawTransaction.createTransaction(nonce, GAS_PRICE, GAS_LIMIT, contractAddress, value,encodedFunction);
//
//        byte[] signedMessage = TransactionEncoder.signMessage(contractTransaction, workerCredentials);
//        String hexString = Numeric.toHexString(signedMessage);
//        EthSendTransaction sendTransaction = client.ethSendRawTransaction(hexString).send();
//        List<Type> someTypes = FunctionReturnDecoder.decode(sendTransaction.getResult(), knightWithdraw.getOutputParameters());
//        Bool b = (Bool) someTypes.get(0);
//        System.out.println(b.getValue());
//        return sendTransaction.getTransactionHash();
        return null;
    }

    public static Event exchangeEvent() {
        Event event = new Event("Exchange", Arrays.asList(
                new TypeReference<Address>(true) {},
                new TypeReference<Uint256>() {},
                new TypeReference<Uint256>() {},
                new TypeReference<Address>() {}));
        return event;
    }

    public static Event couponEvent() {
        Event event = new Event("Coupon", Arrays.asList(
                new TypeReference<Address>(true) {},
                new TypeReference<Uint256>(true) {},
                new TypeReference<Uint256>() {},
                new TypeReference<Uint256>() {}));
        return event;
    }


    /**
     * 编码参数
     * @param parameters
     * @param result
     * @return
     */

    public  static String encodeParameters(List<Type> parameters, StringBuilder result) {
        int dynamicDataOffset = getLength(parameters) * 32;
        StringBuilder dynamicData = new StringBuilder();
        Iterator var4 = parameters.iterator();

        while(var4.hasNext()) {
            Type parameter = (Type)var4.next();
            String encodedValue = TypeEncoder.encode(parameter);
            if (isDynamic(parameter)) {
                String encodedDataOffset = encodeNumeric(new Uint(BigInteger.valueOf((long)dynamicDataOffset)));
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

        while(var2.hasNext()) {
            Type type = (Type)var2.next();
            if (type instanceof StaticArray) {
                count += ((StaticArray)type).getValue().size();
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
            for(int i = 0; i < paddedRawValue.length; ++i) {
                paddedRawValue[i] = paddingValue;
            }
        }

        System.arraycopy(rawValue, 0, paddedRawValue, 32 - rawValue.length, rawValue.length);
        return Numeric.toHexStringNoPrefix(paddedRawValue);
    }

    private static byte getPaddingValue(NumericType numericType) {
        return (byte)(numericType.getValue().signum() == -1 ? -1 : 0);
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
}
