package ru.rsreu.labs.tasks.progress;

public class TaskCompletedEvent<T> {

    private final T result;
    private final TaskProgressInfo<T> source;

    public TaskCompletedEvent(TaskProgressInfo<T> source, T result) {
        this.result = result;
        this.source = source;
    }

    public T getResult() {
        return result;
    }

    public TaskProgressInfo<T> getSource() {
        return source;
    }
}
