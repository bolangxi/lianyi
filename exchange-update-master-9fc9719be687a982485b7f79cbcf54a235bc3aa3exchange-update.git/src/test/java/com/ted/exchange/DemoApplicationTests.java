package com.ted.exchange;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() throws Exception{

//		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
//		requestFactory.setProxy(proxy);
//		RestTemplate template = new RestTemplate(requestFactory);
//		String result = template.getForObject("https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT", String.class);
//		JSONObject jsonObject = new JSONObject(result);
//		System.out.println(jsonObject.get("price"));
	}

	@Autowired 
	private DataDictRepo dataDictRepo;

	@Test
	public void testRepo() {
//		DataDict dataDict = dataDictRepo.findByKey("ethHeight");
//		dataDict.setValue("10001");
//		dataDictRepo.save(dataDict);
	}

}
