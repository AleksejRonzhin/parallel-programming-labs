package ru.rsreu.labs;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;
import ru.rsreu.labs.tasks.progress.GeneralProgress;
import ru.rsreu.labs.tasks.progress.TaskProgress;
import ru.rsreu.labs.tasks.progress.TaskProgressLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PiCalculatingTaskSolver {
    private static final TaskProgressLogger logger = new TaskProgressLogger();
    private static final Function<Double, DoubleUnaryOperator> circleEquation = radius -> x -> Math.sqrt(radius * radius - x * x);


    public double solve(int taskCount, double integrationStep, double radius) throws ExecutionException, InterruptedException {
        IntegralCalculator integralCalculator = new RiemannSumIntegralCalculator(integrationStep);
        Collection<ProgressiveTask> progressiveTasks = createTasks(taskCount, radius, integralCalculator);

        Collection<Future<Double>> futures = progressiveTasks.stream().map(ProgressiveTask::getFuture).collect(Collectors.toList());
        Collection<TaskProgress> taskProgresses = progressiveTasks.stream().map(ProgressiveTask::getProgress).collect(Collectors.toList());

        logGeneralProgress(taskProgresses);
        return sumFutures(futures) * 4 / radius / radius;
    }

    private Collection<ProgressiveTask> createTasks(int taskCount, double radius, IntegralCalculator integralCalculator) {
        ExecutorService service = Executors.newFixedThreadPool(taskCount);
        List<ProgressiveTask> progressiveTasks = new ArrayList<>();
        double calculatingStep = radius / taskCount;

        for (int i = 0; i < taskCount; i++) {
            ProgressiveTask progressiveTask = createTask(i, calculatingStep, radius, integralCalculator, service);
            progressiveTasks.add(progressiveTask);
        }

        service.shutdown();
        return progressiveTasks;
    }

    private ProgressiveTask createTask(int taskNumber, double calculatingStep, double radius, IntegralCalculator integralCalculator, ExecutorService service) {
        final double start = taskNumber * calculatingStep;
        final double end = start + calculatingStep;
        TaskProgress progress = new TaskProgress();
        logger.logProgress(progress, "task " + taskNumber);

        Callable<Double> target = () -> {
            try {
                return integralCalculator.calculate(start, end, circleEquation.apply(radius), progress);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Future<Double> future = service.submit(target);
        return new ProgressiveTask(future, progress);
    }

    private void logGeneralProgress(Collection<TaskProgress> progresses) {
        GeneralProgress generalProgress = new GeneralProgress(progresses);
        logger.logProgress(generalProgress, "general task");
    }

    private double sumFutures(Collection<Future<Double>> futures) throws ExecutionException, InterruptedException {
        double sectorArea = 0;
        for (Future<Double> future : futures) {
            sectorArea += future.get();
        }
        return sectorArea;
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
