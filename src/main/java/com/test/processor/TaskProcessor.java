package com.test.processor;

import com.test.task.Task;
import java.util.concurrent.Future;

public interface TaskProcessor {

    Future<String> calculate(Task task);
    
    int getThreadPoolCapacity();

    void shutdown();

}
