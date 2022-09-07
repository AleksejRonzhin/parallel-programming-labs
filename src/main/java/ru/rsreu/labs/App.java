package ru.rsreu.labs;

import java.util.stream.IntStream;

public class App {
    public static void main(String[] args) {
        double eps = 1E-9;

        IntStream.range(0, 5).parallel().forEach(it -> {
            long startTime = System.currentTimeMillis();
            double pi = new PiCalculator(eps).calculate();
            long endTime = System.currentTimeMillis();
            long resultTime = endTime - startTime;
            System.out.println("Value: " + pi + ". Time: " + resultTime + "ms");
        });

    }
}
