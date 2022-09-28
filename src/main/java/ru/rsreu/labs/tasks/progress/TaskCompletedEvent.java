package ru.rsreu.labs.tasks.progress;

public class TaskCompletedEvent<T> {
    private final TaskProgressInfo<T> source;

    public TaskCompletedEvent(TaskProgressInfo<T> source) {
        this.source = source;
    }

    public TaskProgressInfo<T> getSource() {
        return source;
    }
}
