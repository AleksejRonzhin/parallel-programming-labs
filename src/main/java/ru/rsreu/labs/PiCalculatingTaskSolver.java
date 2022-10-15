package ru.rsreu.labs;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.DoubleUnaryOperator;

public class PiCalculatingTaskSolver {
    private static final TaskProgressLogger logger = new TaskProgressLogger();

    private final IntegralCalculator integralCalculator;
    private final DoubleUnaryOperator circleEquation;
    private final double radius;

    public PiCalculatingTaskSolver(double circleRadius, double integrationStep) {
        this.integralCalculator = new RiemannSumIntegralCalculator(integrationStep);
        this.circleEquation = x -> Math.sqrt(circleRadius * circleRadius - x * x);
        this.radius = circleRadius;
    }

    public double solve(int taskCount) throws InterruptedException, TimeoutException {
        SumStorage sumStorage = new SumStorage();
        ExecutorService executorService = Executors.newFixedThreadPool(taskCount);

        TaskProgress generalProgress = startGeneralTask(divideTask(taskCount, radius), executorService, sumStorage);
        logger.logProgress(generalProgress, "general");

        shutdownWithWaiting(executorService);
        return sumStorage.getSum() * 4 / radius / radius;
    }

    private Iterator<Range> divideTask(int taskCount, double radius) {
        return RangeDivider.divide(new Range(0, radius), taskCount);
    }

    private TaskProgress startGeneralTask(Iterator<Range> rangeIterator, ExecutorService executorService, SumStorage sumStorage) {
        Collection<TaskProgress> progresses = new ArrayList<>();
        rangeIterator.forEachRemaining(range -> {
            TaskProgress progress = executeTaskOnRange(executorService, range, sumStorage);
            progresses.add(progress);
        });
        return new GeneralProgress(progresses);
    }

    private TaskProgress executeTaskOnRange(ExecutorService executorService, Range range, SumStorage sumStorage) {
        TaskProgress progress = new TaskProgress();
        executorService.execute(createTask(range, progress, sumStorage));
        return progress;
    }

    private Runnable createTask(Range range, TaskProgress progress, SumStorage storage) {
        return () -> {
            try {
                integralCalculator.calculate(range, circleEquation, progress, storage);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void shutdownWithWaiting(ExecutorService executorService) throws InterruptedException, TimeoutException {
        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)){
            executorService.shutdownNow();
            throw new TimeoutException();
        }
    }
}