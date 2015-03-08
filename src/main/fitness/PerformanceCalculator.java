package fitness;

import java.util.List;

import model.Chromosome;
import model.Radiography;

public interface PerformanceCalculator {
	/**
	 * 
	 * @param chromosome
	 * @param radiographies
	 * @return the fitness value of the chromosome
	 */
	public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies);

	/**
	 * 
	 * @param performance1
	 * @param performance2
	 * @return true if performance1 is better than performance2, false otherwise
	 */
	public boolean hasBetterPerformance(double performance1, double performance2);
}
