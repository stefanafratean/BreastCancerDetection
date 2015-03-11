package model.performancemeasure;

import fitness.PerformanceCalculator;

public class PerformanceMeasure {
    protected double value = 0;
    protected PerformanceCalculator calculator;

    public PerformanceMeasure() {

    }

    public PerformanceMeasure(PerformanceCalculator calculator, int value) {
        this.calculator = calculator;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public PerformanceCalculator getPerformanceCalculator() {
        return calculator;
    }

    @Override
    public PerformanceMeasure clone() throws CloneNotSupportedException {
        PerformanceMeasure measure = new PerformanceMeasure();
        measure.value = value;
        measure.calculator = calculator;
        return measure;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PerformanceMeasure)) {
            return false;
        }
        PerformanceMeasure other = (PerformanceMeasure) obj;
        return other.value == this.value;
    }
}
