package com.ted.resonance.service.impl;

import com.ted.resonance.service.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class RedisLockImpl implements RedisLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockImpl.class);


    public String LOCK_KEY_PREFIX = "USER_LOCK_";

    public String LOCK_BY_KEY_PREFIX = "RED_PACKET_LOCK_";

    public String LOCKED_VALUE = "TRUE";

    public int LOCKED_TIMEOUT = 60;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public boolean lock(Long userId) {
        String USER_LOCK_KEY = LOCK_KEY_PREFIX + userId;
        boolean locked = redisTemplate.boundValueOps(USER_LOCK_KEY).setIfAbsent(LOCKED_VALUE);
        if (locked) {
            redisTemplate.expire(USER_LOCK_KEY, LOCKED_TIMEOUT, TimeUnit.SECONDS);
        } else {
            logger.info("userId {} is locked now", userId);
        }
        return locked;
    }

    @Override
    public void unlock(Long userId) {
        String USER_LOCK_KEY = LOCK_KEY_PREFIX + userId;
        redisTemplate.delete(USER_LOCK_KEY);
    }

    @Override
    public boolean lockById(Long redPacketId) {
        String PACKET_LOCK_KEY = LOCK_BY_KEY_PREFIX + redPacketId;
        boolean locked = redisTemplate.boundValueOps(PACKET_LOCK_KEY).setIfAbsent(LOCKED_VALUE);
        if (locked) {
            redisTemplate.expire(PACKET_LOCK_KEY, LOCKED_TIMEOUT, TimeUnit.SECONDS);
        } else {
            logger.info("redPacket {} is locked now", PACKET_LOCK_KEY);
        }
        return locked;
    }

    @Override
    public void unlockById(Long redPacketId) {
        String PACKET_LOCK_KEY = LOCK_BY_KEY_PREFIX + redPacketId;
        redisTemplate.delete(PACKET_LOCK_KEY);
    }

    @Override
    public boolean commonLock(String key, int timeout, TimeUnit timeUnit) {
        boolean locked = redisTemplate.boundValueOps(key).setIfAbsent(LOCKED_VALUE);
        if (locked) {
            redisTemplate.expire(key, timeout, timeUnit);
        } else {
            logger.info("commonLock key={} is locked now", key);
        }
        return locked;
    }

    @Override
    public void commonUnLock(String key) {
        redisTemplate.delete(key);
    }
}