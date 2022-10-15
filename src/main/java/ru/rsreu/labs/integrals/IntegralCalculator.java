package ru.rsreu.labs.integrals;

import ru.rsreu.labs.Range;
import ru.rsreu.labs.SumStorage;
import ru.rsreu.labs.tasks.progress.TaskProgress;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {

    void calculate(Range range, DoubleUnaryOperator f, TaskProgress taskProgress, SumStorage sumStorage) throws InterruptedException;
}