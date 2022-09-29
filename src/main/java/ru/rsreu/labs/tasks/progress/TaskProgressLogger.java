package ru.rsreu.labs.tasks.progress;

public class TaskProgressLogger<T> {
    public void logProgress(TaskProgress<T> progress, String taskName) {
        progress.setTaskProgressListener(event -> System.out.printf("Progress %s: %d\n", taskName, event.getProgress()));
    }
}