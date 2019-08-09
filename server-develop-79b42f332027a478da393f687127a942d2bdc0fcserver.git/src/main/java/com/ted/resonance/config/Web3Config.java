package com.ted.resonance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * 配置连接不同的链的bean
 */
@Component
public class Web3Config {
    @Primary
    @Bean(name = "etcClient")
    public Web3j etcClient(){
        return Web3j.build(new HttpService("http://47.111.77.241:8545"));
    }

    @Bean(name = "ethClient")
    public Web3j ethClient(){
        return Web3j.build(new HttpService("http://47.111.77.241:8547"));
    }
}
