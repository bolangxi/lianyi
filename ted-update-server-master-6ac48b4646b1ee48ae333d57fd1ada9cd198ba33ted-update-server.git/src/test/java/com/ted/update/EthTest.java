package com.ted.update;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class EthTest {
    Web3j web3j = Web3j.build(new HttpService("http://47.111.77.241:8547"));

    @Test
    public void testEth() throws IOException {
        EthBlock ethBlock = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(	9118), true).send();
//        System.out.println("JSON.toJSONString(ethBlock.getBlock()) = " + JSON.toJSONString(ethBlock.getBlock()));
        EthGetTransactionReceipt response = web3j.ethGetTransactionReceipt("0xd7dcdae70f4a526bca03d1cfa0230d6f40732db79dc8454250b7c905870e49b6").send();
        System.out.println("response = " + response);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction("").send();
    }
}
