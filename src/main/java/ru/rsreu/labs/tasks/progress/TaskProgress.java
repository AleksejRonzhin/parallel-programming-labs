package ru.rsreu.labs.tasks.progress;

import java.util.*;

public class TaskProgress<T> {
    private final Collection<TaskProgressEventListener<T>> taskProgressListeners = new ArrayList<>();
    protected final ProgressSegmentationHelper progressSegmentationHelper = new ProgressSegmentationHelper();

    public void setProgress(int progress) {
        if (progressSegmentationHelper.limitPassed(progress)) {
            fireTaskProgressEvent(progress);
        }
    }

    public void setTaskProgressListener(TaskProgressEventListener<T> listener) {
        taskProgressListeners.add(listener);
    }

    protected void fireTaskProgressEvent(int progress) {
        TaskProgressEvent<T> event = new TaskProgressEvent<>(this, progress);
        for (TaskProgressEventListener<T> listener : taskProgressListeners) {
            listener.TaskProgressInfo(event);
        }
    }
}