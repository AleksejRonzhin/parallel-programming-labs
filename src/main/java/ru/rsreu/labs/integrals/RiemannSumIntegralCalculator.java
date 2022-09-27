package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator {
    private final double step;


    public RiemannSumIntegralCalculator(double step) {
        this.step = step;
    }

    @Override
    public void calculate(double begin, double end, DoubleUnaryOperator f, TaskProgress<Double> taskProgress) {
        double sum = 0;
        int partCount = (int) ((end - begin) / step);
        taskProgress.init(partCount + 1);
        for (double x = begin; x < end; x += step) {
            double y = f.applyAsDouble(x);
            sum += step * y;
            taskProgress.increment();
        }
        taskProgress.setTaskResult(sum);
    }
}