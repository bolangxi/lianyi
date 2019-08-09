package com.ted.update.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;


/**
 * Created by qingyun.yu on 2019/1/3.
 */
@Configuration
public class BeanConfig {
    @Value("${blockchain.request.url}")
    private String requestUrl;

    @Bean
    public Web3j web3j() {
        return  Web3j.build(new HttpService(requestUrl));
    }
}
