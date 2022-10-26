package ru.rsreu.labs.tasks.progress;

import ru.rsreu.labs.concurrent.Lock;

import java.util.*;

public class GeneralProgress extends TaskProgress {
    private final Map<TaskProgress, Integer> progresses;
    private final Lock locker = new Lock();

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
        try {
            locker.lock();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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