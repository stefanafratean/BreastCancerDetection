package fitness;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.Radiography;

import java.util.ArrayList;
import java.util.List;

public class NegativeClassAccPerformanceCalculator implements PerformanceCalculator{
    private AccPerformanceCalculator accPerformanceCalculator;

    public NegativeClassAccPerformanceCalculator(ChromosomeOutputComputer outputComputer) {
        accPerformanceCalculator = new AccPerformanceCalculator(outputComputer);
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome, List<Radiography> radiographies) {
        List<Radiography> negativeClassRadiographies = getNegativeClassRadiographies(radiographies);
        double positiveClassAccuracy = accPerformanceCalculator.computePerformanceMeasure(chromosome, negativeClassRadiographies);

        return positiveClassAccuracy;
    }

    private List<Radiography> getNegativeClassRadiographies(List<Radiography> radiographies) {
        List<Radiography> negativeClassRadiographies = new ArrayList<Radiography>();
        for (Radiography radiography : radiographies) {
            if (!radiography.isWithCancer()) {
                negativeClassRadiographies.add(radiography);
            }
        }
        return negativeClassRadiographies;
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return accPerformanceCalculator.hasBetterPerformance(performance1, performance2);
    }
}
