package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator {
    private final double step;


    public RiemannSumIntegralCalculator(double step) {
        this.step = step;
    }

    @Override
    public double calculate(double begin, double end, DoubleUnaryOperator f) throws InterruptedException {
        double sum = 0;
        for (double x = begin; x < end; x += step) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            double y = f.applyAsDouble(x);
            sum += step * y;
        }
        return sum;
    }

    @Override
    public double calculate(double begin, double end, DoubleUnaryOperator f, TaskProgress<Double> taskProgress) throws InterruptedException {
        double sum = 0;
        for (double x = begin; x < end; x += step) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            double y = f.applyAsDouble(x);
            sum += step * y;
            int progress = (int) (x / (end - begin) * 100);
            taskProgress.setProgress(progress);
        }
        return sum;
    }
}