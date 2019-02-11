package com.test.processor;

import com.test.task.Task;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskProcessorDefault implements TaskProcessor {

    private final ThreadPoolExecutor executor;

    public TaskProcessorDefault() {
        int maximumPoolSize = 25;
        int queueSize = 25;
        this.executor = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize),
                new ThreadPoolExecutor.AbortPolicy());
    }

    @Override
    public Future<String> calculate(Task task) {
        return executor.submit(task);
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public int getThreadPoolCapacity() {
        return executor.getMaximumPoolSize() + executor.getQueue().remainingCapacity();
    }

}
