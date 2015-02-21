package fitness;

import learning.ChromosomeOperator;
import model.Chromosome;
import model.ConfusionValuesWrapper;
import model.Radiography;
import repository.FitnessHelper;

import java.util.List;

/**
 * Computes the overall accuracy fitness. The optimal fitness is 1, the worst
 * possible fitness value is 0.
 */
public class AccFitnessCalculator implements FitnessCalculator {

    private ChromosomeOperator chromosomeOperator;

    public AccFitnessCalculator(ChromosomeOperator chromosomeOperator) {
        this.chromosomeOperator = chromosomeOperator;
    }

    @Override
    public double computeFitness(Chromosome chromosome,
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
    public boolean isBetterFitness(double fitness1, double fitness2) {
        return FitnessHelper.fitnessAreEqual(fitness1, fitness2)
                || FitnessHelper.fitnessHasBiggerValue(fitness1, fitness2);
    }

}
