package ru.rsreu.labs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class CylinderVolumeCalculatorTest {
    @RepeatedTest(50)
    public void test() {
        double radius = 3;
        double height = 5;
        double expectVolume = 141.37;
        Assertions.assertEquals(expectVolume, CylinderVolumeCalculator.calculate(radius, height), 0.01);
    }
}