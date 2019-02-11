package com.test.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EntityService {
    private Map<String, Lock> lockMap = new HashMap<>();

    public void modifyEntity(String entityId) {
        // use lock with entityId as key to avoid concurrent entity modification
        Lock lock = lockMap.computeIfAbsent(entityId, m -> new ReentrantLock());
        try {
            lock.lock();
            // simulate long entity modification
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
