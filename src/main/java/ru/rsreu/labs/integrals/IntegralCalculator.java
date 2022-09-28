package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgressInfo;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {
    double calculate(double begin, double end, DoubleUnaryOperator f);

    void calculate(double begin, double end, DoubleUnaryOperator f, TaskProgressInfo<Double> taskProgressInfo);
}