package ru.rsreu.labs.tasks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

public class TaskProgress<T> {
    private final Collection<Function<T, T>> resultMappers = new LinkedList<>();
    private T taskResult;
    private int maxProgress;
    private int currentProgress;
    private String taskName;

    public void init(int max) {
        this.maxProgress = max;
    }

    public void increment() {
        currentProgress++;
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
    }

    public int getPercent() {
        if (maxProgress == 0) return 0;
        return (int) ((double) currentProgress / maxProgress * 100);
    }

    public void addMapper(Function<T, T> mapper) {
        this.resultMappers.add(mapper);
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}