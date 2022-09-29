package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.function.Function;

public class GeneralProgress<T> extends TaskProgress<T> {
    private final Map<TaskProgress<T>, Integer> progresses;

    public GeneralProgress(Collection<TaskProgress<T>> progresses, Function<Collection<T>, T> resultMapper) {
        this.progresses = new HashMap<>();
        for (TaskProgress<T> progress : progresses) {
            this.progresses.put(progress, 0);
        }
        for (TaskProgress<T> progress : progresses) {
            progress.setTaskProgressListener(event -> {
                this.progresses.put(event.getSource(), event.getProgress());
                updateGeneralProgress();
            });
        }
    }

    private void updateGeneralProgress() {
        int progress = (int) this.progresses.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
        if (progressSegmentationHelper.limitPassed(progress)) fireTaskProgressEvent(progress);
    }
}