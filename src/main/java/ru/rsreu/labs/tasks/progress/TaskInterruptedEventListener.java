package ru.rsreu.labs.tasks.progress;

public interface TaskInterruptedEventListener<T> {
    void taskInterruptedEvent(TaskInterruptedEvent<T> event);
}