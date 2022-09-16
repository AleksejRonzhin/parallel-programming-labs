package ru.rsreu.labs;

import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        double step = 1E-9;
        double radius = 2;

        IntStream.range(0, 5).forEach(it -> {
            long startTime = System.currentTimeMillis();
            double pi = new CircleAreaPiCalculator(step, radius).calculate();
            long endTime = System.currentTimeMillis();
            long resultTime = endTime - startTime;
            System.out.println("Value: " + pi + ". Time: " + resultTime + "ms");
        });

    }
}
