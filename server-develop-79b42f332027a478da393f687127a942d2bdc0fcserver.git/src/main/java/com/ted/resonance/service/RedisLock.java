package com.ted.resonance.service;

import java.util.concurrent.TimeUnit;

public interface RedisLock {

    boolean lock(Long userId);

    void unlock(Long userId);

    boolean commonLock(String key, int timeout, TimeUnit timeUnit);

    void commonUnLock(String key);

    boolean lockById(Long redPacketId);

    void unlockById(Long redPacketId);

}
