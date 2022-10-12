package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GeneralProgress extends TaskProgress {
    private final Map<TaskProgress, Integer> progresses;
    private final Lock locker = new ReentrantLock();

    public GeneralProgress(Collection<TaskProgress> progresses) {
        this.progresses = new HashMap<>();
        for (TaskProgress progress : progresses) {
            this.progresses.put(progress, 0);
        }

        for (TaskProgress progress : progresses) {
            progress.setTaskProgressListener(this::taskProgressEventHandler);
        }
    }

    private void taskProgressEventHandler(TaskProgressEvent event){
        locker.lock();
        try {
            this.progresses.put(event.getSource(), event.getProgress());
            updateGeneralProgress();
        } finally {
            locker.unlock();
        }
    }

    private void updateGeneralProgress() {
        int progress = (int) this.progresses.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
        if (progressSegmentationHelper.limitPassed(progress)) fireTaskProgressEvent(progress);
    }
}