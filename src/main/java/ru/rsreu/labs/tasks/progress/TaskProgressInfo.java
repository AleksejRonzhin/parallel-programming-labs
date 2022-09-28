package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.function.Function;

public class TaskProgressInfo<T> {
    protected final Collection<TaskProgressInfoEventListener<T>> listeners = new ArrayList<>();
    protected final ProgressSegmentationHelper progressSegmentationHelper = new ProgressSegmentationHelper();
    private final Deque<Function<T, T>> resultMappers = new LinkedList<>();
    protected long startTime;
    protected long endTime;
    private T taskResult;

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
        fireTaskCompletedEvent(res);
    }

    public Optional<Long> getTime(){
        if(startTime == 0 || endTime == 0) return Optional.empty();
        return Optional.of(endTime - startTime);
    }

    public void addMapper(Function<T, T> mapper) {
        this.resultMappers.addFirst(mapper);
    }

    public void setProgress(int progress) {
        if (progressSegmentationHelper.limitPassed(progress)) {
            fireTaskProgressInfoEvent(progress);
        }
    }

    public void stopTask() {
        //fireTaskProgressInfoEvent(String.format("%s stopped", taskName));
    }

    public void setListener(TaskProgressInfoEventListener<T> listener) {
        listeners.add(listener);
    }

    protected void fireTaskProgressInfoEvent(int progress) {
        TaskProgressEvent<T> event = new TaskProgressEvent<>(this, progress);
        for (TaskProgressInfoEventListener<T> listener : listeners) {
            listener.taskProgressInfo(event);
        }
    }

    protected void fireTaskCompletedEvent(T result) {
        TaskCompletedEvent<T> event = new TaskCompletedEvent<>(this, result);
        for (TaskProgressInfoEventListener<T> listener : listeners) {
            listener.taskCompleted(event);
        }
    }

    public void startTiming() {
        startTime = System.currentTimeMillis();
    }
}