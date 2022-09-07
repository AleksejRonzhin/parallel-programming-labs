package ru.rsreu.labs;

public class CylinderVolumeCalculator {
    static double calculate(double radius, double height) {
        double square = Math.PI * Math.pow(radius, 2);
        return square * height;
    }
}
