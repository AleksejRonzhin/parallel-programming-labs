package ru.rsreu.labs;

public class PiCalculatingCreator implements TargetCreator{
    private static final double STEP = 1E-9;

    public  Runnable create(String[] args){
        return () -> {
            int radius = Integer.parseInt(args[0]);
            long startTime = System.currentTimeMillis();
            double pi = new CircleAreaPiCalculator(STEP, radius).calculate();
            long endTime = System.currentTimeMillis();
            long resultTime = endTime - startTime;
            System.out.println(Thread.currentThread().getName() + ": Value: " + pi + ". Time: " + resultTime + "ms");
        };
    }
}
