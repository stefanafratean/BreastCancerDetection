package model.objective;

import fitness.AccPerformanceCalculator;
import fitness.PerformanceCalculator;
import model.Chromosome;

public class AccObjective extends MaximizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            PerformanceCalculator performanceCalculator = chromosome1.getPerformanceMeasures().get(i).getPerformanceCalculator();
            if (performanceCalculator instanceof AccPerformanceCalculator) {
                double accChromosome1 = chromosome1.getPerformanceMeasures().get(i).getValue();
                double accChromosome2 = chromosome2.getPerformanceMeasures().get(i).getValue();
                return Double.valueOf(accChromosome1).compareTo(accChromosome2);
            }
        }
        return 0;
    }
}
