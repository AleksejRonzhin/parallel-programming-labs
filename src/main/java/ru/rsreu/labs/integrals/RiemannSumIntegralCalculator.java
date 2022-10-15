package ru.rsreu.labs.integrals;

import ru.rsreu.labs.Range;
import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator {
    private final double step;


    public RiemannSumIntegralCalculator(double step) {
        this.step = step;
    }


    @Override
    public double calculate(Range range, DoubleUnaryOperator f, TaskProgress taskProgress) throws InterruptedException {
        double sum = 0;
        for (double x = range.getStart(); x < range.getEnd(); x += step) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            double y = f.applyAsDouble(x);
            sum += step * y;
            int progress = (int) ((x - range.getStart()) / range.getLength() * 100) + 1;
            taskProgress.setProgress(progress);
        }
        return sum;
    }
}