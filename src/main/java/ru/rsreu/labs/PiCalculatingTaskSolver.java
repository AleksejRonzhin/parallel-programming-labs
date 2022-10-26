package ru.rsreu.labs;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;
import ru.rsreu.labs.concurrent.CountDownLatch;
import ru.rsreu.labs.concurrent.Semaphore;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.function.DoubleUnaryOperator;

import static java.util.stream.Collectors.toList;
import static ru.rsreu.labs.FutureUtils.sumFutures;

public class PiCalculatingTaskSolver {
    private static final TaskProgressLogger logger = new TaskProgressLogger();
    private final IntegralCalculator integralCalculator;
    private final DoubleUnaryOperator circleEquation;
    private final Semaphore semaphore;
    private final double radius;

    public PiCalculatingTaskSolver(double circleRadius, double integrationStep, int permits) {
        this.integralCalculator = new RiemannSumIntegralCalculator(integrationStep);
        this.circleEquation = x -> Math.sqrt(circleRadius * circleRadius - x * x);
        this.radius = circleRadius;
        this.semaphore = new Semaphore(permits);
    }

    public double solve(int taskCount) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(taskCount);
        CountDownLatch latch = new CountDownLatch(taskCount);

        Collection<ProgressiveTask> progressiveTasks = startGeneralTask(divideTask(taskCount, radius), executorService, latch);

        Collection<Future<Double>> futures = progressiveTasks.stream().map(ProgressiveTask::getFuture).collect(toList());
        Collection<TaskProgress> taskProgresses = progressiveTasks.stream().map(ProgressiveTask::getProgress).collect(toList());

        logger.logProgress(new GeneralProgress(taskProgresses), "general");
        return sumFutures(futures) * 4 / radius / radius;
    }

    private Iterator<Range> divideTask(int taskCount, double radius) {
        return RangeDivider.divide(new Range(0, radius), taskCount);
    }

    private Collection<ProgressiveTask> startGeneralTask(Iterator<Range> rangeIterator, ExecutorService executorService, CountDownLatch latch) {
        Collection<ProgressiveTask> progressiveTasks = new ArrayList<>();
        int numberTask = 1;
        while (rangeIterator.hasNext()) {
            ProgressiveTask progressiveTask = submitTaskOnRange(executorService, rangeIterator.next(), latch, numberTask++);
            progressiveTasks.add(progressiveTask);
        }
        return progressiveTasks;
    }

    private ProgressiveTask submitTaskOnRange(ExecutorService executorService, Range range, CountDownLatch latch, int numberTask) {
        TaskProgress progress = new TaskProgress();
        logger.logProgress(progress, numberTask + " task");
        Future<Double> future = executorService.submit(createTask(range, progress, latch, numberTask));
        return new ProgressiveTask(future, progress);
    }

    private Callable<Double> createTask(Range range, TaskProgress progress, CountDownLatch latch, int numberTask) {
        return () -> {
            try {
                double result = callWithSemaphore(
                        () -> integralCalculator.calculate(range, circleEquation, progress)
                );
                System.out.printf("Delay task %d: %dms.\n", numberTask, getDelay(latch));
                return result;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private <T> T callWithSemaphore(Callable<T> callable) throws Exception {
        semaphore.acquire();
        try {
            return callable.call();
        }
        finally {
            semaphore.release();
        }
    }

    private long getDelay(CountDownLatch latch) throws InterruptedException {
        latch.countDown();
        long startTime = System.currentTimeMillis();
        latch.await();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private static class ProgressiveTask {
        private final Future<Double> future;
        private final TaskProgress progress;

        public ProgressiveTask(Future<Double> future, TaskProgress progress) {
            this.future = future;
            this.progress = progress;
        }

        public Future<Double> getFuture() {
            return future;
        }

        public TaskProgress getProgress() {
            return progress;
        }
    }
}
