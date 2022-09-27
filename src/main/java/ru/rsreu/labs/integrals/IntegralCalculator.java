package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {
    void calculate(double begin, double end, DoubleUnaryOperator f, TaskProgress<Double> taskProgress);
}