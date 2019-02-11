package com.test.processor;

import com.test.task.Task;
import java.util.concurrent.Future;

/**
 * Please place your solution here
 */
public class TaskProcessorExperimental implements TaskProcessor {

    @Override
    public Future<String> calculate(Task task) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public int getThreadPoolCapacity() {
        return 0;
    }

    @Override
    public void shutdown() {

    }
}
