package model.objective;

import fitness.PerformanceCalculator;
import fitness.PositiveClassAccPerformanceCalculator;
import model.Chromosome;

public class PositiveClassAccObjective extends MaximizableObjective {
    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            PerformanceCalculator performanceCalculator = chromosome1.getPerformanceMeasures().get(i).getPerformanceCalculator();
            if (performanceCalculator instanceof PositiveClassAccPerformanceCalculator) {
                double accChromosome1 = chromosome1.getPerformanceMeasures().get(i).getValue();
                double accChromosome2 = chromosome2.getPerformanceMeasures().get(i).getValue();
                return Double.valueOf(accChromosome1).compareTo(accChromosome2);
            }
        }
        return 0;
    }
}
