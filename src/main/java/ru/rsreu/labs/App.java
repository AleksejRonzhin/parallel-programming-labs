package ru.rsreu.labs;

import ru.rsreu.labs.integrals.CircleAreaPiCalculator;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressInfo;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;

public class App {

    public static void main(String[] args) {
        TaskProgressInfo<Double> progressInfo = new TaskProgressInfo<>();
        TaskProgressInfo<Double> progressInfo2 = new TaskProgressInfo<>();
        TaskProgressInfo<Double> progressInfo3 = new TaskProgressInfo<>();
        Collection<TaskProgressInfo<Double>> progresses = new ArrayList<>();
        progresses.add(progressInfo);
        progresses.add(progressInfo2);
        progresses.add(progressInfo3);
        TaskProgressLogger<Double> logger = new TaskProgressLogger<>();
        GeneralProgress<Double> generalProgress = new GeneralProgress<>(progresses, App::sum);
        logger.logProgress(generalProgress);
        generalProgress.startTiming();
        new Thread(() -> {
            new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo);
        }).start();

        new Thread(() -> {
            new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo3);
        }).start();
        new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo2);
    }

    private static double sum(Collection<Double> values){
        return values.stream().mapToDouble(Double::doubleValue).sum();
    }
}
