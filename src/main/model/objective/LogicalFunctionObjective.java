package model.objective;

import fitness.LogicalFunctionPerformanceCalculator;
import fitness.WmwPerformanceCalculator;
import model.Chromosome;

public class LogicalFunctionObjective extends MaximizableObjective {

    @Override
    protected int compareChromosomes(Chromosome chromosome1, Chromosome chromosome2) {
        for (int i = 0; i < chromosome1.getPerformanceMeasures().size(); i++) {
            if (chromosome1.getPerformanceMeasures().get(i).getPerformanceCalculator() instanceof LogicalFunctionPerformanceCalculator) {
                double valueChr1 = chromosome1.getPerformanceMeasures().get(i).getValue();
                double valueChr2 = chromosome2.getPerformanceMeasures().get(i).getValue();
                return Double.valueOf(valueChr1).compareTo(valueChr2);
            }
        }
        return 0;
    }
}
