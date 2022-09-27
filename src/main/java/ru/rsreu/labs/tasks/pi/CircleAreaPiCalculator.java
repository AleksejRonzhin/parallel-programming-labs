package ru.rsreu.labs.tasks.pi;

import ru.rsreu.labs.integrals.IntegralCalculator;
import ru.rsreu.labs.integrals.RiemannSumIntegralCalculator;
import ru.rsreu.labs.tasks.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public class CircleAreaPiCalculator {
    private final IntegralCalculator integralCalculator;
    private final double radius;

    public CircleAreaPiCalculator(double eps, double radius) {
        this.integralCalculator = new RiemannSumIntegralCalculator(eps);
        this.radius = radius;
    }

    public void calculate(TaskProgress<Double> taskProgress) {
        taskProgress.addMapper(circleArea -> circleArea / (radius * radius));
        calculateCircleArea(taskProgress);
    }

    private void calculateCircleArea(TaskProgress<Double> taskProgress) {
        DoubleUnaryOperator circleEquation = x -> Math.sqrt(radius * radius - x * x);
        taskProgress.addMapper(sectorArea -> sectorArea * 4);
        integralCalculator.calculate(0, radius, circleEquation, taskProgress);
    }
}