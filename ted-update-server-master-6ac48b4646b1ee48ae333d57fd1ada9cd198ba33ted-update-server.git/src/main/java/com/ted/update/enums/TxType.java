package com.ted.update.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.logging.log4j.util.Strings;
import org.springframework.util.CollectionUtils;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public enum TxType {
    TRANSFER("0xa9059cbb", new DefaultParseTransfer(), "转账")
    , BURN("0x42966c68", new DefaultParseTransfer(), "燃烧")
    , EXCHANGE("0xa9b3f97a", new DefaultParseTransfer(), "炼金");
    private String methodId;
    private String description;
    private ParseTransfer parseTransfer;


    TxType(String methodId, ParseTransfer parseTransfer, String description) {
        this.methodId = methodId;
        this.description = description;
        this.parseTransfer = parseTransfer;
    }

    public static TxType txType(String methodId) {
        try{
            return Stream.of(TxType.values()).filter(txType -> txType.methodId.equals(methodId)).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Type> parse(EthGetTransactionReceipt transactionReceipt) {
        return parseTransfer.parse(transactionReceipt);
    }
}

interface ParseTransfer{
    List<Type> parse(EthGetTransactionReceipt transactionReceipt);
}

class DefaultParseTransfer implements ParseTransfer {

    @Override
    public List<Type> parse(EthGetTransactionReceipt transactionReceipt) {
        if (Objects.nonNull(transactionReceipt)) {
            TransactionReceipt receipt = transactionReceipt.getResult();
            if (Objects.nonNull(receipt)) {
                List<Log> logs = receipt.getLogs();
                if (!CollectionUtils.isEmpty(logs)) {
                    Log log = logs.get(0);
                    List<String> topics = log.getTopics();
                    if (topics.size() >= 3) {
                        String data = log.getData();
                        String topics1 = topics.get(1);
                        String topics2 = topics.get(2);
                        if (Strings.isNotBlank(topics1) && Strings.isNotBlank(topics2)) {
                            String rawInput = new StringBuffer(topics1)
                                    .append(topics2.replace("0x", ""))
                                    .append(data.replace("0x", ""))
                                    .toString();
                            Function function = new Function("transfer", Collections.<Type>emptyList(),
                                    Arrays.asList(new TypeReference<Address>() {
                                    }, new TypeReference<Address>() {
                                    }, new TypeReference<Uint256>() {
                                    }));
                            return FunctionReturnDecoder.decode(rawInput, function.getOutputParameters());
                        }
                    }
                }
            }
        }
        return null;
    }
}