package ru.rsreu.labs.tasks.progress;

public class TaskProgressEvent<T> {
    private final int progress;
    private final TaskProgress<T> source;

    public TaskProgressEvent(TaskProgress<T> source, int progress) {
        this.progress = progress;
        this.source = source;
    }

    public int getProgress() {
        return progress;
    }

    public TaskProgress<T> getSource() {
        return source;
    }
}

