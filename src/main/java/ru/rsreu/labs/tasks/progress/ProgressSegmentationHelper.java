package ru.rsreu.labs.tasks.progress;

public class ProgressSegmentationHelper {
    private final static int MIN_PROGRESS = 0;
    private final static int MAX_PROGRESS = 100;
    private final static int DEFAULT_LIMITS_COUNT = 10;
    private final int[] progressLimits;
    private int nextLimitIndex = 0;

    public ProgressSegmentationHelper(int stepCount) {
        this.progressLimits = createProgressLimits(stepCount);
    }

    public ProgressSegmentationHelper() {
        this.progressLimits = createProgressLimits(DEFAULT_LIMITS_COUNT);
    }

    static private int[] createProgressLimits(int stepCount) {
        if (stepCount < 2) return new int[0];
        int[] limits = new int[stepCount];
        int step = (MAX_PROGRESS - MIN_PROGRESS) / stepCount;
        for (int i = 0; i < stepCount - 1; i++) {
            limits[i] = MIN_PROGRESS + i * step + step;
        }
        limits[stepCount - 1] = MAX_PROGRESS;
        return limits;
    }

    public boolean limitPassed(int progress) {
        if (nextLimitIndex >= progressLimits.length) return false;

        if (progress < progressLimits[nextLimitIndex]) return false;

        while (progress >= progressLimits[nextLimitIndex]) {
            nextLimitIndex++;
            if (nextLimitIndex >= progressLimits.length) return true;
        }
        return true;
    }
}