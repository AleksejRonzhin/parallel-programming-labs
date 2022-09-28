package ru.rsreu.labs.tasks.progress;

public class TaskProgressLogger<T> {
    public void logProgress(TaskProgressInfo<T> task) {
        task.setTaskProgressListener(event -> {
            System.out.printf("Progress %s: %d\n", event.getSource().getTaskName(), event.getProgress());
        });
    }
}