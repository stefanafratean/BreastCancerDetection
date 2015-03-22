package fitness;

import learning.ChromosomeOutputComputer;
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

    private ChromosomeOutputComputer outputComputer;

    public AccPerformanceCalculator(ChromosomeOutputComputer outputComputer) {
        this.outputComputer = outputComputer;
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies) {
        ConfusionValuesWrapper confusionValues = new ConfusionValuesWrapper();
        for (Radiography r : radiographies) {
            double chromosomeOutput = outputComputer.getOutputValue(
                    chromosome, r);
            predictValue(confusionValues,
                    FitnessHelper.itHasCancer(chromosomeOutput),
                    r.isWithCancer());
        }
        return confusionValues.getOverallAccuracy();
    }

    private void predictValue(ConfusionValuesWrapper confusionValues,
                              boolean predictedPositive, boolean actualPositive) {
        if (predictedPositive && actualPositive) {
            confusionValues.increaseTP();
        } else if (predictedPositive && !actualPositive) {
            confusionValues.increaseFP();
        } else if (!predictedPositive && !actualPositive) {
            confusionValues.increaseTN();
        } else {
            confusionValues.increaseFN();
        }
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return FitnessHelper.fitnessAreEqual(performance1, performance2)
                || FitnessHelper.fitnessHasBiggerValue(performance1, performance2);
    }
}
