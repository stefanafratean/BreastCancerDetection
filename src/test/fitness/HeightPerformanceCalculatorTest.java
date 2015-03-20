package fitness;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HeightPerformanceCalculatorTest {

    @Test
    public void less_higher_is_better() {
        PerformanceCalculator calculator = new HeightPerformanceCalculator();
        assertTrue(calculator.hasBetterPerformance(1, 2));
    }

    @Test
    public void higher_is_worse() {
        PerformanceCalculator calculator = new HeightPerformanceCalculator();
        assertFalse(calculator.hasBetterPerformance(2, 1));
    }

}