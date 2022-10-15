package ru.rsreu.labs;

public class Range {
    private final double start;
    private final double end;

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Range{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public Range(double start, double end) {
        this.start = start;
        this.end = end;
    }

    public double getLength() {
        return end - start;
    }
}