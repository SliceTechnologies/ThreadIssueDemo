package com.test.processor;

import com.test.task.Task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Based on 2nd executor for immediate tasks.
 * These tasks will not be blocked by current hanging tasks
 */
public class TaskProcessorExperimental implements TaskProcessor {

    private final ThreadPoolExecutor hangingExecutor;
    private final ThreadPoolExecutor immediateExecutor;

    public TaskProcessorExperimental() {
        int maximumPoolSize = 25;
        int queueSize = 25;
        this.hangingExecutor = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), new ThreadPoolExecutor.AbortPolicy());
        int immediateExecutorPoolSize = 5;
        this.immediateExecutor = new ThreadPoolExecutor(immediateExecutorPoolSize, immediateExecutorPoolSize, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), new ThreadPoolExecutor.AbortPolicy());
        System.out.println("TaskProcessorExperimental is initialized");
    }

    @Override
    public Future<String> calculate(Task task) {
        if (task.getEntityId().startsWith("h")) {
            return hangingExecutor.submit(task);
        } else {
            return immediateExecutor.submit(task);
        }
    }

    @Override
    public void shutdown() {
        hangingExecutor.shutdown();
        immediateExecutor.shutdown();
    }

    @Override
    public int getThreadPoolCapacity() {
        return hangingExecutor.getMaximumPoolSize() + hangingExecutor.getQueue().remainingCapacity();
    }
}
