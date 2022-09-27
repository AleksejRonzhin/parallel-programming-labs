package ru.rsreu.labs;

import ru.rsreu.labs.tasks.TaskProgress;

import java.util.ArrayList;
import java.util.Collection;

public class Logger {
    private final Collection<TaskProgress<Double>> taskProgresses = new ArrayList<>();

    public void logProgress(TaskProgress<Double> task) {
        taskProgresses.add(task);
    }

    public void start() {
        Thread logThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                taskProgresses.removeIf(progress -> {
                    if (progress.getTaskResult().isPresent()) {
                        System.out.printf("%s finished. Result: %s\n", progress.getTaskName(), progress.getTaskResult().get());
                        return true;
                    }
                    return false;
                });
            }
        });
        logThread.setDaemon(true);
        logThread.start();
    }
}