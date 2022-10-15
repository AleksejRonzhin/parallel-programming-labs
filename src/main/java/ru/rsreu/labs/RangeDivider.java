package ru.rsreu.labs;

import java.util.Iterator;

public class RangeDivider {

    public static Iterator<Range> divide(Range range, int partCount){
        return new RangeIterator(range, partCount);
    }

    private static class RangeIterator implements Iterator<Range>{
        private double current;
        private final double end;
        private final double step;

        private RangeIterator(Range range, int partCount){
            this.current = range.getStart();
            this.end = range.getEnd();
            this.step = range.getLength() / partCount;
        }

        @Override
        public boolean hasNext() {
            return current + step / 2 < end;
        }

        @Override
        public Range next() {
            return new Range(current, current += step);
        }
    }
}