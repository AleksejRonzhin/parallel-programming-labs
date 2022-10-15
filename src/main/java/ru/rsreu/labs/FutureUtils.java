package ru.rsreu.labs;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureUtils {
    static double sumFutures(Collection<Future<Double>> futures) throws ExecutionException, InterruptedException {
        double sum = 0;
        for (Future<Double> future : futures) {
            sum += future.get();
        }
        return sum;
    }
}