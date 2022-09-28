package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.function.Function;

public class TaskProgressInfo<T> {
    private final Deque<Function<T, T>> resultMappers = new LinkedList<>();
    private final Collection<TaskProgressEventListener<T>> taskProgressListeners = new ArrayList<>();

    private final Collection<TaskCompletedEventListener<T>> taskCompletedListeners = new ArrayList<>();

    private final Collection<TaskInterruptedEventListener<T>> taskInterruptedListeners = new ArrayList<>();
    private final ProgressSegmentationHelper progressSegmentationHelper = new ProgressSegmentationHelper();
    private T taskResult;
    private String taskName;
    private long startTime;
    private long endTime;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Optional<T> getTaskResult() {
        if (this.taskResult == null) return Optional.empty();
        return Optional.of(this.taskResult);
    }

    public void setTaskResult(T taskResult) {
        T res = taskResult;
        for (Function<T, T> mapper : resultMappers) {
            res = mapper.apply(res);

        }
        this.taskResult = res;
        endTime = System.currentTimeMillis();
        fireTaskCompletedEvent();
    }

    public void addMapper(Function<T, T> mapper) {
        this.resultMappers.addFirst(mapper);
    }

    public void setProgress(int progress) {
        if (progressSegmentationHelper.limitPassed(progress)) {
            fireTaskProgressEvent(progress);
        }
    }

    public void stopTask() {
        fireTaskInterruptedEvent();
    }

    public void setTaskProgressListener(TaskProgressEventListener<T> listener) {
        taskProgressListeners.add(listener);
    }

    public void setTaskCompletedListener(TaskCompletedEventListener<T> listener) {
        taskCompletedListeners.add(listener);
    }

    public void setTaskInterruptedListeners(TaskInterruptedEventListener<T> listener) {
        taskInterruptedListeners.add(listener);
    }

    private void fireTaskProgressEvent(int progress) {
        TaskProgressEvent<T> event = new TaskProgressEvent<>(this, progress);
        for (TaskProgressEventListener<T> listener : taskProgressListeners) {
            listener.TaskProgressInfo(event);
        }
    }

    private void fireTaskInterruptedEvent() {
        TaskInterruptedEvent<T> event = new TaskInterruptedEvent<>(this);
        for (TaskInterruptedEventListener<T> listener : taskInterruptedListeners) {
            listener.taskInterruptedEvent(event);
        }
    }

    private void fireTaskCompletedEvent() {
        TaskCompletedEvent<T> event = new TaskCompletedEvent<>(this);
        for (TaskCompletedEventListener<T> listener : taskCompletedListeners) {
            listener.taskCompletedEvent(event);
        }
    }

    public void startTiming() {
        startTime = System.currentTimeMillis();
        endTime = startTime;
    }

    public Optional<Long> getTime() {
        if (startTime == 0 || endTime == 0) return Optional.empty();

        return Optional.of(endTime - startTime);
    }
}