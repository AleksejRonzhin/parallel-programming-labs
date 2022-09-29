package ru.rsreu.labs.tasks.progress;

public interface TaskProgressEventListener<T> {
    void TaskProgressInfo(TaskProgressEvent<T> event);
}
