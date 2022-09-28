package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgressInfo;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator {
    private final double step;


    public RiemannSumIntegralCalculator(double step) {
        this.step = step;
    }

    @Override
    public double calculate(double begin, double end, DoubleUnaryOperator f) {
        double sum = 0;
        for (double x = begin; x < end; x += step) {
            double y = f.applyAsDouble(x);
            sum += step * y;
        }
        return sum;
    }

    @Override
    public void calculate(double begin, double end, DoubleUnaryOperator f, TaskProgressInfo<Double> taskProgressInfo) {
        taskProgressInfo.startTiming();
        double sum = 0;
        for (double x = begin; x < end; x += step) {
            if(Thread.interrupted()){
                taskProgressInfo.stopTask();
                return;
            }
            double y = f.applyAsDouble(x);
            sum += step * y;
            int progress = (int) (x / (end - begin) * 100);
            taskProgressInfo.setProgress(progress);
        }
        taskProgressInfo.setTaskResult(sum);
    }
}