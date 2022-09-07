package ru.rsreu.labs;

import java.util.function.DoubleUnaryOperator;

public interface IntegralCalculator {
    double calculate(double begin, double end, DoubleUnaryOperator f);
}