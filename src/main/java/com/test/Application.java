package com.test;

import com.test.processor.TaskProcessorFactory;
import com.test.processor.TaskProcessor;
import com.test.service.EntityService;
import com.test.task.Task;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Application {

    public static void main(String[] args)
            throws ExecutionException, InterruptedException, TimeoutException {
        String mode = args.length > 0 ? args[0] : "default";
        TaskProcessor taskProcessor = TaskProcessorFactory.getInstance(mode);
        int threadPoolCapacity = taskProcessor.getThreadPoolCapacity();
        System.out.println("threadPoolCapacity " + threadPoolCapacity);

        final ArrayList<Future<String>> resultFutures = new ArrayList<>();

        final EntityService entityService = new EntityService();
        for (int i = 0; i < threadPoolCapacity-1; i++) {
            final Task task = new Task(entityService, "hanging_entity_id");
            resultFutures.add(taskProcessor.calculate(task));
        }

        final Future<String> immediateTaskFuture;
        // Now executor is fully busy - both threads and queue are occupied by 50 events with the same marker
        try {
            immediateTaskFuture = taskProcessor
                    .calculate(new Task(entityService, "immediate_entity_id"));
            // this brings to exception even though task itself would be processed immediately
        } catch (RejectedExecutionException e) {
            System.out.println("Failed due to threadpool executor blocking.");
            throw e;
        }

        // each task shouldn't take more that 1000ms, so 2000ms must be enough
        System.out.println(immediateTaskFuture.get(2000, TimeUnit.MILLISECONDS));

        for (Future<String> resultFuture : resultFutures) {
            System.out.println(">> " + resultFuture.get());
        }
        taskProcessor.shutdown();
    }
}
