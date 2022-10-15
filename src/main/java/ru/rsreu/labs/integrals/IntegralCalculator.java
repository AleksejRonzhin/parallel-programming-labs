package ru.rsreu.labs.integrals;

import ru.rsreu.labs.Range;
import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {

    double calculate(Range range, DoubleUnaryOperator f, TaskProgress taskProgress) throws InterruptedException;
}