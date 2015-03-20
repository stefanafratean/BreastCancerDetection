package fitness;

import model.Chromosome;
import model.Radiography;

import java.util.List;

public class HeightPerformanceCalculator implements PerformanceCalculator {
    @Override
    public double computePerformanceMeasure(Chromosome chromosome, List<Radiography> radiographies) {
        return chromosome.getDepth();
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return performance1 < performance2;
    }
}
