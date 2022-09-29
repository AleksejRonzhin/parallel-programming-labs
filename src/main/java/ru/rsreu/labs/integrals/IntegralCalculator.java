package ru.rsreu.labs.integrals;

import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {
    double calculate(double begin, double end, DoubleUnaryOperator f) throws InterruptedException;

    double calculate(double begin, double end, DoubleUnaryOperator f, TaskProgress taskProgress) throws InterruptedException;
}