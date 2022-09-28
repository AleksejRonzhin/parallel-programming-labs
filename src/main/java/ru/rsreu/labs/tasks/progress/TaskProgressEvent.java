package ru.rsreu.labs.tasks.progress;

public class TaskProgressEvent<T> {
    private final int progress;
    private final TaskProgressInfo<T> source;

    public TaskProgressEvent(TaskProgressInfo<T> source, int progress) {
        this.progress = progress;
        this.source = source;
    }

    public int getProgress() {
        return progress;
    }

    public TaskProgressInfo<T> getSource() {
        return source;
    }
}

