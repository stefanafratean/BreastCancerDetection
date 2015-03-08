package model.objective;

import fitness.WmwPerformanceCalculator;
import model.Chromosome;

public class WmwObjective extends MaximizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            if (chromosome1.getPerformanceMeasures().get(i).getPerformanceCalculator() instanceof WmwPerformanceCalculator) {
                double wmwChromosome1 = chromosome1.getPerformanceMeasures().get(i).getValue();
                double wmwChromosome2 = chromosome2.getPerformanceMeasures().get(i).getValue();
                return Double.valueOf(wmwChromosome1).compareTo(wmwChromosome2);
            }
        }
        return 0;
    }
}
