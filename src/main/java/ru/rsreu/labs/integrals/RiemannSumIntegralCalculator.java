package ru.rsreu.labs.integrals;

import java.util.function.DoubleUnaryOperator;

public class RiemannSumIntegralCalculator implements IntegralCalculator{
    private final static int PART_COUNT = 10;
    private final double step;


    public RiemannSumIntegralCalculator(double step){
        this.step = step;
    }

    @Override
    public double calculate(double begin, double end, DoubleUnaryOperator f) {
        double sum = 0;
        double part = (end - begin) / PART_COUNT;
        for(int i = 0; i < PART_COUNT; i++){
            double beginPart = begin + i * part;
            double endPart = begin + (i + 1) * part;
            for (double x = beginPart; x < endPart; x += step) {
                double y = f.applyAsDouble(x);
                sum += step * y;
            }
            System.out.printf("Progress: %d%%\n", (i + 1) * 10);
        }
        return sum;
    }
}