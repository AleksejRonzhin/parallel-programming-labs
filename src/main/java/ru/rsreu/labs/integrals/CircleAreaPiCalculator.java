package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgressInfo;

import java.util.function.DoubleUnaryOperator;

public class CircleAreaPiCalculator {
    private final IntegralCalculator integralCalculator;
    private final double radius;

    public CircleAreaPiCalculator(double eps, double radius) {
        this.integralCalculator = new RiemannSumIntegralCalculator(eps);
        this.radius = radius;
    }

    public void calculate(TaskProgressInfo<Double> taskProgressInfo) {
        taskProgressInfo.addMapper(circleArea -> circleArea / (radius * radius));
        calculateCircleArea(taskProgressInfo);
    }

    public double calculate(){
        return calculateCircleArea() / (radius * radius);
    }

    private void calculateCircleArea(TaskProgressInfo<Double> taskProgressInfo) {
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        taskProgressInfo.addMapper(sectorArea -> sectorArea * 4);
        integralCalculator.calculate(0, radius, circleEquation, taskProgressInfo);
    }

    private double calculateCircleArea(){
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        return integralCalculator.calculate(0, radius, circleEquation) * 4;
    }
}