package ru.rsreu.labs.integrals;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator{
    private final double step;

    public RiemannSumIntegralCalculator(double step){
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
}