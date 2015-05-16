package fitness;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.Radiography;

import java.util.ArrayList;
import java.util.List;

public class PositiveClassAccPerformanceCalculator implements PerformanceCalculator {
    private AccPerformanceCalculator accPerformanceCalculator;

    public PositiveClassAccPerformanceCalculator(ChromosomeOutputComputer outputComputer) {
        accPerformanceCalculator = new AccPerformanceCalculator(outputComputer);
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome, List<Radiography> radiographies) {
        List<Radiography> positiveClassRadiographies = getPositiveClassRadiographies(radiographies);
        double positiveClassAccuracy = accPerformanceCalculator.computePerformanceMeasure(chromosome, positiveClassRadiographies);

        return positiveClassAccuracy;
    }

    private List<Radiography> getPositiveClassRadiographies(List<Radiography> radiographies) {
        List<Radiography> positiveClassRadiographies = new ArrayList<Radiography>();
        for (Radiography radiography : radiographies) {
            if (radiography.isWithCancer()) {
                positiveClassRadiographies.add(radiography);
            }
        }
        return positiveClassRadiographies;
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return accPerformanceCalculator.hasBetterPerformance(performance1, performance2);
    }
}
