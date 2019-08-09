package com.ted.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import javax.transaction.Transactional;
import java.net.InetSocketAddress;
import java.net.Proxy;

//标注服务层，主要用来进行业务的逻辑处理
@Service
public class DataDictService {
    @Autowired
    private DataDictRepo dataDictRepo;

    @Autowired @Qualifier("etcClient")
    private Web3j etcClient;

    @Autowired @Qualifier("ethClient")
    private Web3j ethClient;

    @Value("${TEDUSDT}") private String TEDUSDT;


    //每8秒更新一次 区块高度
    @Scheduled(fixedDelay = 8000)
    public void updateHeight() throws Exception{
        DataDict ethHeight = dataDictRepo.findByKey("ethHeight");
        DataDict etcHeight = dataDictRepo.findByKey("etcHeight");
        if(etcHeight == null) {
            etcHeight = new DataDict();
            etcHeight.setKey("etcHeight");
        }
        if(ethHeight == null) {
            ethHeight = new DataDict();
            ethHeight.setKey("ethHeight");
        }
        etcHeight.setValue(etcClient.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getNumber().toString());
        ethHeight.setValue(ethClient.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getNumber().toString());
        dataDictRepo.save(etcHeight);
        dataDictRepo.save(ethHeight);
    }

    //每天更新一次  汇率
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCurrency() throws Exception{
        updateRate("ethRate", "ETHUSDT");
        updateRate("etcRate", "ETCUSDT");
        //ted  目前配置文件写死
        updateRate("tedRate", "TEDUSDT");
    }

    private DataDict updateRate(String key, String symbol) throws Exception{
        //币安屏蔽大陆 ip， 生产环境应该部署到非大陆服务器， 本地调试用代理
        //@todo  部署到测试环境要注释掉
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
        requestFactory.setProxy(proxy);
        RestTemplate template = new RestTemplate(requestFactory);

//        RestTemplate template = new RestTemplate();

        DataDict dataDict = dataDictRepo.findByKey(key);
        if(dataDict == null) {
            dataDict = new DataDict();
            dataDict.setKey(key);
        }
        if("tedRate".equals(key)) {
            dataDict.setValue(TEDUSDT);
            return null;
        }
        String result = template.getForObject("https://api.binance.com/api/v3/ticker/price?symbol=" + symbol, String.class);
        JSONObject jsonObject = new JSONObject(result);
        dataDict.setValue((String)jsonObject.get("price"));
        return dataDict;
    }
}
