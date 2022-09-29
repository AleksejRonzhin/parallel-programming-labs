package ru.rsreu.labs.tasks.progress;

import java.util.*;

public class TaskProgress {
    private final Collection<TaskProgressEventListener> taskProgressListeners = new ArrayList<>();
    protected final ProgressSegmentationHelper progressSegmentationHelper = new ProgressSegmentationHelper();

    public void setProgress(int progress) {
        if (progressSegmentationHelper.limitPassed(progress)) {
            fireTaskProgressEvent(progress);
        }
    }

    public void setTaskProgressListener(TaskProgressEventListener listener) {
        taskProgressListeners.add(listener);
    }

    protected void fireTaskProgressEvent(int progress) {
        TaskProgressEvent event = new TaskProgressEvent(this, progress);
        for (TaskProgressEventListener listener : taskProgressListeners) {
            listener.TaskProgressInfo(event);
        }
    }
}