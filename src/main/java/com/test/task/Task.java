package com.test.task;

import com.test.service.EntityService;
import java.util.concurrent.Callable;

public class Task implements Callable<String> {

    private final EntityService entityService;
    private final String entityId;

    public Task(EntityService entityService, String entityId) {
        this.entityService = entityService;
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public String call() {
        entityService.modifyEntity(entityId);
        return entityId;
    }
}
