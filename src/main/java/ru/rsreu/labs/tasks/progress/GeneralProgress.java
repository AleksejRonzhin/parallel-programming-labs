package ru.rsreu.labs.tasks.progress;

import java.util.*;
import java.util.function.Function;

public class GeneralProgress<T> extends TaskProgressInfo<T> {
    private final Map<TaskProgressInfo<T>, Integer> progresses;
    private final Function<Collection<T>, T> resultMapper;
    private final Collection<T> results = new ArrayList<>();

    public GeneralProgress(Collection<TaskProgressInfo<T>> progresses, Function<Collection<T>, T> resultMapper) {
        this.resultMapper = resultMapper;
        this.progresses = new HashMap<>();
        for (TaskProgressInfo<T> progress : progresses) {
            this.progresses.put(progress, 0);
        }
        for (TaskProgressInfo<T> progress : progresses) {
            progress.setListener(new GeneralProgressListener());
        }
    }

    private void updateGeneralProgress() {
        int progress = (int) this.progresses.values().stream().mapToInt(Integer::intValue).average().orElse(0.0);
        if (progressSegmentationHelper.limitPassed(progress)) fireTaskProgressInfoEvent(progress);
    }

    private void updateResult() {
        Optional<T> result = getTaskResult();
        if (result.isPresent()) {
            endTime = System.currentTimeMillis();
            setTaskResult(result.get());
        }
    }

    @Override
    public Optional<T> getTaskResult() {
        if (results.size() != progresses.size()) return Optional.empty();
        return Optional.of(resultMapper.apply(results));
    }

    private class GeneralProgressListener implements TaskProgressInfoEventListener<T> {
        @Override
        public void taskProgressInfo(TaskProgressEvent<T> event) {
            synchronized (GeneralProgress.this) {
                progresses.put(event.getSource(), event.getProgress());
                updateGeneralProgress();
            }
        }

        @Override
        public void taskCompleted(TaskCompletedEvent<T> event) {
            synchronized (GeneralProgress.this) {
                results.add(event.getResult());
                updateResult();
            }
        }
    }
}
