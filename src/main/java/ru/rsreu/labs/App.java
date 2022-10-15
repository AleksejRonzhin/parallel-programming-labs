package ru.rsreu.labs;

import java.util.concurrent.ExecutionException;

public class App {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        double pi = new PiCalculatingTaskSolver(10, 1E-9, 3).solve(6);
        long endTime = System.currentTimeMillis();
        System.out.printf("Result: %s. Time: %d ms.", pi, endTime - startTime);
    }
}
