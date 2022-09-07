package ru.rsreu.labs;

import java.util.function.DoubleUnaryOperator;

public class PiCalculator {
    private final IntegralCalculator integralCalculator;

    public PiCalculator(double eps) {
        this.integralCalculator = new RiemannSumIntegralCalculator(eps);
    }

    public double calculate() {
        double radius = 1;
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        double sectorArea = integralCalculator.calculate(0, 1, circleEquation);
        double circleArea = sectorArea * 4;
        return circleArea / (radius * radius);
    }
}
