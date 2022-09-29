package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class CircleAreaPiCalculator {
    private final IntegralCalculator integralCalculator;
    private final double radius;

    public CircleAreaPiCalculator(double eps, double radius) {
        this.integralCalculator = new RiemannSumIntegralCalculator(eps);
        this.radius = radius;
    }

    public double calculate(TaskProgress<Double> taskProgress) throws InterruptedException {
        return calculateCircleArea(taskProgress) / (radius * radius);
    }

    public double calculate() throws InterruptedException {
        return calculateCircleArea() / (radius * radius);
    }

    private double calculateCircleArea(TaskProgress<Double> taskProgress) throws InterruptedException {
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        double sectorArea = integralCalculator.calculate(0, radius, circleEquation, taskProgress);
        return sectorArea * 4;
    }

    private double calculateCircleArea() throws InterruptedException {
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        double sectorArea = integralCalculator.calculate(0, radius, circleEquation);
        return sectorArea * 4;
    }
}