package ru.rsreu.labs;

public class SumStorage {
    private double sum = 0;

    public synchronized void add(double value){
        sum += value;
    }

    public synchronized double getSum() {
        return sum;
    }
}