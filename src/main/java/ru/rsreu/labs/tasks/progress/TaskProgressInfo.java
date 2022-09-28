package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.function.Function;

public class TaskProgressInfo<T> {
    private final Deque<Function<T, T>> resultMappers = new LinkedList<>();
    private final Collection<TaskProgressInfoEventListener> listeners = new ArrayList<>();
    private final ProgressSegmentationHelper progressSegmentationHelper = new ProgressSegmentationHelper();
    private T taskResult;
    private String taskName;
    private long startTime;

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
        long time = System.currentTimeMillis() - startTime;
        fireTaskProgressInfoEvent(String.format("Result %s: %s. Time: %d ms", this.taskName, this.taskResult, time));
    }

    public void addMapper(Function<T, T> mapper) {
        this.resultMappers.addFirst(mapper);
    }

    public void setProgress(int progress) {
        if (progressSegmentationHelper.limitPassed(progress)) {
            fireTaskProgressInfoEvent(String.format("Progress %s: %d%%", this.taskName, progress));
        }
    }

    public void stopTask() {
        fireTaskProgressInfoEvent(String.format("%s stopped", taskName));
    }

    public void setListener(TaskProgressInfoEventListener listener) {
        listeners.add(listener);
    }

    private void fireTaskProgressInfoEvent(String message) {
        TaskProgressInfoEvent event = new TaskProgressInfoEvent(this, message);
        for (TaskProgressInfoEventListener listener : listeners) {
            listener.TaskProgressInfo(event);
        }
    }

    public void startTiming() {
        startTime = System.currentTimeMillis();
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}