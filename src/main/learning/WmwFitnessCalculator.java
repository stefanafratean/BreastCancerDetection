package learning;

import java.util.ArrayList;
import java.util.List;

import repository.FitnessHelper;
import model.Chromosome;
import model.Radiography;

public class WmwFitnessCalculator implements FitnessCalculator {

	@Override
	public double computeFitness(Chromosome chromosome,
			List<Radiography> radiographies) {
		List<Double> positiveClassOutputs = new ArrayList<Double>();
		List<Double> negativeClassOutputs = new ArrayList<Double>();

		for (Radiography rad : radiographies) {
			double output = ChromosomeOperator.getOuputValue(chromosome, rad);
			if (rad.isWithCancer()) {
				positiveClassOutputs.add(output);
			} else {
				negativeClassOutputs.add(output);
			}
		}
		
		double sum = 0;
		for (int i = 0; i < positiveClassOutputs.size(); i++){
			for (int j = 0; j < negativeClassOutputs.size(); j++){
				if (positiveClassOutputs.get(i) > 0 && positiveClassOutputs.get(i) > negativeClassOutputs.get(j)){
					sum++;
				}
			}
		}

		return (double) sum / (double)(positiveClassOutputs.size() * negativeClassOutputs.size());
	}

	@Override
	public boolean isBetterFitness(double fitness1, double fitness2) {
		return FitnessHelper.fitnessHasBiggerValue(fitness1, fitness2);
		//		|| FitnessHelper.fitnessAreEqual(fitness1, fitness2);
	}

}
