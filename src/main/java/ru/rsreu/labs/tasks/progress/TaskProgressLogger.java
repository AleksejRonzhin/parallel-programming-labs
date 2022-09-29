package ru.rsreu.labs.tasks.progress;

public class TaskProgressLogger {
    public void logProgress(TaskProgress progress, String taskName) {
        progress.setTaskProgressListener(event -> System.out.printf("Progress %s: %d\n", taskName, event.getProgress()));
    }
}