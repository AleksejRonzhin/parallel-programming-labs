package ru.rsreu.labs.tasks.progress;

public class TaskInterruptedEvent<T> {
    private final TaskProgressInfo<T> source;

    public TaskInterruptedEvent(TaskProgressInfo<T> source) {
        this.source = source;
    }

    public TaskProgressInfo<T> getSource() {
        return source;
    }
}