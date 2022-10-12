package ru.rsreu.labs;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;

public class PiCalculatingTaskSolver {
    private static final TaskProgressLogger logger = new TaskProgressLogger();
    private static final Function<Double, DoubleUnaryOperator> circleEquation = radius -> x -> Math.sqrt(radius * radius - x * x);

    public double solve(int taskCount, double integrationStep, double radius) throws InterruptedException {
        IntegralCalculator integralCalculator = new RiemannSumIntegralCalculator(integrationStep);

        Collection<Thread> threads = new ArrayList<>();
        Collection<TaskProgress> progresses = new ArrayList<>();

        double calculatingStep = radius / taskCount;
        SumStorage sumStorage = new SumStorage();
        for (int i = 0; i < taskCount; i++) {
            TaskProgress progress = new TaskProgress();
            progresses.add(progress);

            final double start = i * calculatingStep;
            final double end = start + calculatingStep;

            Runnable target = () -> {
                try {
                    integralCalculator.calculate(start, end, circleEquation.apply(radius), progress, sumStorage);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            };

            Thread thread = new Thread(target);
            threads.add(thread);
            thread.start();
        }

        GeneralProgress generalProgress = new GeneralProgress(progresses);
        logger.logProgress(generalProgress, "general");

        for(Thread thread: threads){
            thread.join();
        }

        return sumStorage.getSum() * 4 / radius / radius;
    }
}