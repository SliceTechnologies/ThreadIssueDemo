package com.test.processor;

import com.test.task.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Based on EXTRA_POOL_CAPACITY for immediate tasks
 */
public class TaskProcessorExperimental implements TaskProcessor {

    private static final int DEFAULT_POOL_CAPACITY = 50;
    private static final int EXTRA_POOL_CAPACITY = 50;
    private final ThreadPoolExecutor executor;

    public TaskProcessorExperimental() {
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Available cores = " + cores);
        int queueSize = DEFAULT_POOL_CAPACITY - cores + EXTRA_POOL_CAPACITY;
        this.executor = new ThreadPoolExecutor(cores, cores, 0, TimeUnit.SECONDS, new PriorityBlockingQueue<>(queueSize, (o1, o2) -> {
            if (!(o1 instanceof CustomRunnableFuture) ||!(o2 instanceof CustomRunnableFuture)) {
                return 0;
            }
            CustomRunnableFuture crf1 = (CustomRunnableFuture) o1;
            CustomRunnableFuture crf2 = (CustomRunnableFuture) o2;
            // 'I' will have higher priority than 'H'
            return crf2.getEntityId().compareTo(crf1.getEntityId());
        }),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public Future<String> calculate(Task task) {
        CustomRunnableFuture runnableFuture = new CustomRunnableFuture(task, task.getEntityId());
        executor.execute(runnableFuture);
        return runnableFuture;
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public int getThreadPoolCapacity() {
        return DEFAULT_POOL_CAPACITY;
    }

    private static class CustomRunnableFuture extends FutureTask<String> {

        private String entityId;

        public CustomRunnableFuture(Callable<String> callable, String entityId) {
            super(callable);
            this.entityId = entityId;
        }

        public String getEntityId() {
            return entityId;
        }
    }

}
