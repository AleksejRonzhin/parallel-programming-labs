package ru.rsreu.labs;

import ru.rsreu.labs.integrals.CircleAreaPiCalculator;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;

public class App {

    public static void main(String[] args) {
        TaskProgress progressInfo = new TaskProgress();
        TaskProgress progressInfo2 = new TaskProgress();
        TaskProgress progressInfo3 = new TaskProgress();
        Collection<TaskProgress> progresses = new ArrayList<>();
        progresses.add(progressInfo);
        progresses.add(progressInfo2);
        progresses.add(progressInfo3);
        TaskProgressLogger logger = new TaskProgressLogger();
        GeneralProgress generalProgress = new GeneralProgress(progresses);
        logger.logProgress(generalProgress, "general task");
        logger.logProgress(progressInfo, "first task");

        new Thread(() -> {
            try {
                new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }}).start();
        new Thread(() -> {
            try {
                new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }}).start();
        new Thread(() -> {
            try {
                new CircleAreaPiCalculator(1E-9, 1).calculate(progressInfo2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }}).start();

    }

    private static double sum(Collection<Double> values){
        return values.stream().mapToDouble(Double::doubleValue).sum();
    }
}
