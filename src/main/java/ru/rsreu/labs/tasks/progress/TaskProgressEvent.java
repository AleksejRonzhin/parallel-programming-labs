package ru.rsreu.labs.tasks.progress;

public class TaskProgressEvent {
    private final int progress;
    private final TaskProgress source;

    public TaskProgressEvent(TaskProgress source, int progress) {
        this.progress = progress;
        this.source = source;
    }

    public int getProgress() {
        return progress;
    }

    public TaskProgress getSource() {
        return source;
    }
}

