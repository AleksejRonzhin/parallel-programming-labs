package ru.rsreu.labs.integrals;

import ru.rsreu.labs.Range;
import ru.rsreu.labs.SumStorage;
import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator {
    private final double step;


    public RiemannSumIntegralCalculator(double step) {
        this.step = step;
    }

    @Override
    public void calculate(Range range, DoubleUnaryOperator f, TaskProgress taskProgress, SumStorage sumStorage) throws InterruptedException {
        for (double x = range.getStart(); x < range.getEnd(); x += step) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            double y = f.applyAsDouble(x);
            sumStorage.add(y * step);

            int progress = (int) ((x - range.getStart()) / range.getLength() * 100) + 1;
            taskProgress.setProgress(progress);
        }
    }
}