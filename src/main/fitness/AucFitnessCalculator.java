package fitness;

import java.math.BigDecimal;
import java.util.List;

import learning.ChromosomeOperator;
import learning.FitnessCalculator;
import model.AucValuesWrapper;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

public class AucFitnessCalculator implements FitnessCalculator {

	@Override
	public double computeFitness(Chromosome chromosome,
			List<Radiography> radiographies) {
		AucValuesWrapper aucValues = new AucValuesWrapper();
		computeOutputs(chromosome, radiographies, aucValues);
		double auc = 0;
		int tresholdPointsNb = calculateRates(aucValues);

		auc = computeAuc(aucValues, auc, tresholdPointsNb);
		return auc;
	}

	private double computeAuc(AucValuesWrapper aucValues, double auc,
			int tresholdPointsNb) {
		aucValues.sortRates();
		for (int i = 0; i < tresholdPointsNb - 1; i++) {
			auc += 0.5d * (aucValues.getFp(i + 1) - aucValues.getFp(i))
					* (aucValues.getTp(i + 1) + aucValues.getTp(i));
		}
		return auc;
	}

	private int calculateRates(AucValuesWrapper aucValues) {
		aucValues.sortOutputCollections();
		int tresholdPointsNb = 0;
		BigDecimal increment = new BigDecimal(0.1d);
		for (BigDecimal i = BigDecimal.ZERO; i.compareTo(BigDecimal.ONE) < 0; i = i.add(increment)) {
			aucValues.addFp(computeFpRate(i, aucValues));
			aucValues.addTp(computeTpRate(i, aucValues));
			tresholdPointsNb++;
		}
		return tresholdPointsNb;
	}

	private void computeOutputs(Chromosome chromosome,
			List<Radiography> radiographies, AucValuesWrapper aucValues) {
		for (Radiography r : radiographies) {
			double output = ChromosomeOperator.getOuputValue(chromosome, r);
			if (r.isWithCancer()) {
				aucValues.addCancerOutput(output);
			} else {
				aucValues.addNormalOutput(output);
			}
		}
		aucValues.sortOutputCollections();
	}

	private double computeTpRate(BigDecimal treshold, AucValuesWrapper aucValues) {
		return computeRateForClass(aucValues.getOutputsForCancerRadios(),
				treshold);
	}

	private double computeFpRate(BigDecimal treshold, AucValuesWrapper aucValues) {
		return computeRateForClass(aucValues.getOutputsForNormalRadios(),
				treshold);
	}

	private double computeRateForClass(List<Double> listForClass,
			BigDecimal treshold) {
		double number = 0;
		for (Double output : listForClass) {
			if (output.compareTo(treshold.doubleValue()) >= 0) {
				number++;
			}
		}
		if (listForClass.isEmpty()) {
			return 0;
		}
		return number / listForClass.size();
	}

	@Override
	public boolean isBetterFitness(double fitness1, double fitness2) {
		return FitnessHelper.fitnessAreEqual(fitness1, fitness2)
				|| FitnessHelper.fitnessHasBiggerValue(fitness1, fitness2);
	}

}
