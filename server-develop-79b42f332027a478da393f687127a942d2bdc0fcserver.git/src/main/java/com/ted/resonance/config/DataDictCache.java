package com.ted.resonance.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.ted.resonance.entity.DataDictBean;
import com.ted.resonance.mapper.DataDictBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: zzm
 * @Date: 2019-07-16 18:14
 * @Description:
 */
@Component
public class DataDictCache {


    @Value("${guava.capacity}")
    private int capacity = 10;
    @Value("${guava.maximum-size}")
    private int maximumSize = 100;
    @Value("${guava.over-time}")
    private Long overTime = 3500L;//缓存过期时间
    @Value("${guava.concurrency-level}")
    private int concurrencyLevel = 8;

    private static LoadingCache<String, String> loadingCache;

    @Autowired
    private DataDictBeanMapper dataDictBeanMapper;

    @PostConstruct
    public void init() {
        loadingCache = CacheBuilder.newBuilder()
                .initialCapacity(capacity)
                .maximumSize(maximumSize)
                .concurrencyLevel(concurrencyLevel)
                .expireAfterAccess(overTime, TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) {
                        DataDictBean bean = dataDictBeanMapper.queryByKey(key);
                        if (bean != null) {
                            return bean.getValue();
                        }
                        return "";
                    }
                });
    }


    /**
     * 获取配置的值
     *
     * @param key
     * @return
     */
    public static String getDataValue(String key) {
        return loadingCache.getUnchecked(key);
    }


}
