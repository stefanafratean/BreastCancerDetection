package model.objective;

import fitness.WmwNegativePerformanceCalculator;
import model.Chromosome;

public class WmwNegativeObjective extends MaximizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            if (chromosome1.getPerformanceMeasures().get(i).getPerformanceCalculator() instanceof WmwNegativePerformanceCalculator) {
                double wmwChromosome1 = chromosome1.getPerformanceMeasures().get(i).getValue();
                double wmwChromosome2 = chromosome2.getPerformanceMeasures().get(i).getValue();
                return Double.valueOf(wmwChromosome1).compareTo(wmwChromosome2);
            }
        }
        return 0;
    }
}
