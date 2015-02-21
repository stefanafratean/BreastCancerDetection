package fitness;

import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

import java.util.List;

/**
 * Computes the fitness as the sum of false positives and false negatives. The
 * optimal fitness is 0, the worst possible fitness is equal with the number of
 * processed radiographies
 */
public class WrongDecisionsFitnessCalculator implements FitnessCalculator {

    private ChromosomeOperator chromosomeOperator;

    public WrongDecisionsFitnessCalculator(ChromosomeOperator chromosomeOperator) {
        this.chromosomeOperator = chromosomeOperator;
    }

    @Override
    public double computeFitness(Chromosome chromosome,
                                 List<Radiography> radiographies) {
        double fitness = 0;
        for (Radiography r : radiographies) {
            double chromosomeOutput = chromosomeOperator.getOutputValue(
                    chromosome, r);
            if (madeWrongDecision(r, chromosomeOutput)) {
                fitness++;
            }
        }
        return fitness;
    }

    @Override
    public boolean isBetterFitness(double fitness1, double fitness2) {
        return FitnessHelper.fitnessAreEqual(fitness1, fitness2)
                || !FitnessHelper.fitnessHasBiggerValue(fitness1, fitness2);
    }

    private static boolean madeWrongDecision(Radiography r,
                                             double chromosomeOutput) {
        return FitnessHelper.getCancerDecision(chromosomeOutput) != r
                .isWithCancer();
    }

}
