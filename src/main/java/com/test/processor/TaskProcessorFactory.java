package com.test.processor;

public class TaskProcessorFactory {

    public static TaskProcessor getInstance(String mode) {
        switch (mode) {
        case "experiment":
            return new TaskProcessorExperimental();
        case "default":
            return new TaskProcessorDefault();
        default:
            throw new IllegalArgumentException("Wrong mode: " + mode);
        }
    }
}
