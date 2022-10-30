package ru.rsreu.labs.concurrent;

public class SumStorage {
    private int sum = 0;

    public void add(int value){
        sum += value;
    }

    public int getSum(){
        return sum;
    }

}
