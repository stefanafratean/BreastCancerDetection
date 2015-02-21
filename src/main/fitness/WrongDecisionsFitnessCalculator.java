package fitness;

import java.util.List;

import learning.ChromosomeOperator;
import learning.FitnessCalculator;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

/**
 * 
 * Computes the fitness as the sum of false positives and false negatives. The
 * optimal fitness is 0, the worst possible fitness is equal with the number of
 * processed radiographies
 * 
 */
public class WrongDecisionsFitnessCalculator implements FitnessCalculator {

	@Override
	public double computeFitness(Chromosome chromosome,
			List<Radiography> radiographies) {
		double fitness = 0;
		for (Radiography r : radiographies) {
			double chromosomeOutput = ChromosomeOperator.getOuputValue(
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
