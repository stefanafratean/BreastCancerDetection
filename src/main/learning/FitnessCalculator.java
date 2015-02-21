package learning;

import java.util.List;

import model.Chromosome;
import model.Radiography;

public interface FitnessCalculator {
	/**
	 * 
	 * @param chromosome
	 * @param radiographies
	 * @return the fitness value of the chromosome
	 */
	public double computeFitness(Chromosome chromosome,
			List<Radiography> radiographies);

	/**
	 * 
	 * @param fitness1
	 * @param fitness2
	 * @return true if fitness1 is better than fitness2, false otherwise
	 */
	public boolean isBetterFitness(double fitness1, double fitness2);
}
