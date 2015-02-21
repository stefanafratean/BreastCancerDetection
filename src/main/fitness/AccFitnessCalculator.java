package fitness;

import java.util.List;

import learning.ChromosomeOperator;
import learning.FitnessCalculator;
import model.Chromosome;
import model.ConfuzionValuesWrapper;
import model.Radiography;
import repository.FitnessHelper;

/**
 * 
 * Computes the overall accuracy fitness. The optimal fitness is 1, the worst
 * possible fitness value is 0.
 * 
 */
public class AccFitnessCalculator implements FitnessCalculator {

	@Override
	public double computeFitness(Chromosome chromosome,
			List<Radiography> radiographies) {
		ConfuzionValuesWrapper confuzionValues = new ConfuzionValuesWrapper();
		for (Radiography r : radiographies) {
			double chromosomeOutput = ChromosomeOperator.getOuputValue(
					chromosome, r);
			predictValue(confuzionValues,
					FitnessHelper.getCancerDecision(chromosomeOutput),
					r.isWithCancer());
		}
		return confuzionValues.getOverallAccuracy();
	}

	private static void predictValue(ConfuzionValuesWrapper confuzionValues,
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
