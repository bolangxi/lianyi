package com.ted.exchange;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * 配置连接不同的链的bean
 */
 
 
 //把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>
@Component
public class Web3Config {
    @Value("${etcNode}") 
	private String etcNode;
    @Value("${ethNode}") 
	private String ethNode;
	
    @Primary
    @Bean(name = "etcClient")
    public Web3j etcClient(){
        return Web3j.build(new HttpService(etcNode));
    }

    @Bean(name = "ethClient")
    public Web3j ethClient(){
        return Web3j.build(new HttpService(ethNode));
    }
}
