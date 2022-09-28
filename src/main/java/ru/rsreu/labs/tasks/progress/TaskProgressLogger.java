package ru.rsreu.labs.tasks.progress;

public class TaskProgressLogger {
    public void logProgress(TaskProgressInfo<?> task) {
        task.setListener(event -> System.out.println(event.getMessage()));
    }
}