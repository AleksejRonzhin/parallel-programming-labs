package ru.rsreu.labs;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;

import java.util.function.DoubleUnaryOperator;

public class CircleAreaPiCalculator {
    private final IntegralCalculator integralCalculator;
    private final double radius;

    public CircleAreaPiCalculator(double eps, double radius) {
        this.integralCalculator = new RiemannSumIntegralCalculator(eps);
        this.radius = radius;
    }

    public double calculate() {
        return calculateCircleArea() / (radius * radius);
    }

    private double calculateCircleArea(){
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        double sectorArea = integralCalculator.calculate(0, radius, circleEquation);
        return sectorArea * 4;
    }
}