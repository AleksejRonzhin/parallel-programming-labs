package ru.rsreu.labs;

import java.util.concurrent.TimeoutException;

public class App {

    public static void main(String[] args) throws InterruptedException, TimeoutException {
        long startTime = System.currentTimeMillis();
        double pi = new PiCalculatingTaskSolver(1, 1E-8).solve(5);
        long endTime = System.currentTimeMillis();
        System.out.printf("Result: %s. Time: %d ms.", pi, endTime - startTime);
    }
}
