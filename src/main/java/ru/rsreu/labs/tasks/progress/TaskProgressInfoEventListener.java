package ru.rsreu.labs.tasks.progress;

public interface TaskProgressInfoEventListener<T> {
    void taskProgressInfo(TaskProgressEvent<T> event);

    void taskCompleted(TaskCompletedEvent<T> event);
}