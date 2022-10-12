package ru.rsreu.labs;

import java.util.concurrent.ExecutionException;

public class App {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        double pi = new PiCalculatingTaskSolver().solve(6, 1E-9, 10);
        long endTime = System.currentTimeMillis();
        System.out.printf("Result: %s. Time: %d ms.", pi, endTime - startTime);
    }
}
