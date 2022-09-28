package ru.rsreu.labs.tasks.progress;

public interface TaskCompletedEventListener<T> {
    void taskCompletedEvent(TaskCompletedEvent<T> event);
}
