package ru.rsreu.labs;

public class App {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        double pi = new PiCalculatingTaskSolver().solve(5, 1E-8, 1);
        long endTime = System.currentTimeMillis();
        System.out.printf("Result: %s. Time: %d ms.", pi, endTime - startTime);

    }
}
