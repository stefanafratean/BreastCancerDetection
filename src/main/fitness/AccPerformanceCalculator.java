package fitness;

import learning.ChromosomeOperator;
import model.Chromosome;
import model.ConfusionValuesWrapper;
import model.Radiography;
import repository.FitnessHelper;

import java.util.List;

/**
 * Computes the overall accuracy. The optimal value is 1, the worst
 * possible value is 0.
 */
public class AccPerformanceCalculator implements PerformanceCalculator {

    private static PerformanceCalculator instance;
    private ChromosomeOperator chromosomeOperator;

    public AccPerformanceCalculator() {

    }

    public AccPerformanceCalculator(ChromosomeOperator chromosomeOperator) {
        this.chromosomeOperator = chromosomeOperator;
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies) {
        ConfusionValuesWrapper confusionValues = new ConfusionValuesWrapper();
        for (Radiography r : radiographies) {
            double chromosomeOutput = chromosomeOperator.getOutputValue(
                    chromosome, r);
            predictValue(confusionValues,
                    FitnessHelper.getCancerDecision(chromosomeOutput),
                    r.isWithCancer());
        }
        return confusionValues.getOverallAccuracy();
    }

    private static void predictValue(ConfusionValuesWrapper confuzionValues,
                                     boolean predictedPositive, boolean actualpositive) {
        if (predictedPositive && actualpositive) {
            confuzionValues.increaseTP();
        } else if (predictedPositive && !actualpositive) {
            confuzionValues.increaseFP();
        } else if (!predictedPositive && !actualpositive) {
            confuzionValues.increaseTN();
        } else {
            confuzionValues.increaseFN();
        }
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return FitnessHelper.fitnessAreEqual(performance1, performance2)
                || FitnessHelper.fitnessHasBiggerValue(performance1, performance2);
    }

    public static PerformanceCalculator getInstance() {
        if (instance == null) {
            return new AccPerformanceCalculator();
        }
        return instance;
    }
}
