package repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import learning.ChromosomeOperator;
import fitness.FitnessCalculator;
import fitness.WmwFitnessCalculator;
import model.Chromosome;
import model.Radiography;

public class ChromosomeRepository {
	public static final int POPULATION_NUMBER = 100;
	// private static final int TOURNAMENT_NUMBER = 5 / 100 * POPULATION_NUMBER;
	private static final int TOURNAMENT_NUMBER = 20;
	private final List<Chromosome> population;
//	 private Chromosome worst;
//	 private Chromosome best;
	private final Random r;
	private final FitnessCalculator fitnessCalculator;

	public ChromosomeRepository(Random r, ChromosomeOperator chromosomeOperator) {
		this.r = r;
		fitnessCalculator = new WmwFitnessCalculator(chromosomeOperator);
		population = new ArrayList<Chromosome>();
		initPopulation(chromosomeOperator);
	}

	// initialize population with the method ramped half-half
	private void initPopulation(ChromosomeOperator chromosomeOperator) {
		for (int i = 0; i < POPULATION_NUMBER / 2; i++) {
			population.add(chromosomeOperator.createChromosome(
					chromosomeOperator.MAX_CHROMOSOME_DEPTH, r, false));
			population.add(chromosomeOperator.createChromosome(
					chromosomeOperator.MAX_CHROMOSOME_DEPTH, r, true));
		}
	}

	/**
	 * Determines the best and the worst chromosomes from the population. The
	 * best chromosome from the population is considered the one that has the
	 * smallest fitness, the worst chromosome is the one that has the biggest
	 * fitness.
	 */
//	 public void evaluatePopulation() {
//	 worst = population.get(0);
//	 best = population.get(0);
//	 for (Chromosome chromosome : population) {
//	 if (isBetterThanCurrentBest(chromosome)) {
//	 best = chromosome;
//	 }
//	 if (isWorseThanCurrentWorst(chromosome)) {
//	 worst = chromosome;
//	 }
//	 }
//	 }

//	private boolean isWorseThanCurrentWorst(Chromosome chromosome) {
//		return !fitnessCalculator.isBetterFitness(chromosome.getFitness(),
//				worst.getFitness());
//	}
//
//	private boolean isBetterThanCurrentBest(Chromosome chromosome) {
//		return fitnessCalculator.isBetterFitness(chromosome.getFitness(),
//				best.getFitness());
//	}

	/**
	 * Randomly selects a chromosome from the population
	 */
	public Chromosome selectParent() {
		// Chromosome choiceA = population.get(r.nextInt(POPULATION_NUMBER));
		// Chromosome choiceB = population.get(r.nextInt(POPULATION_NUMBER));
		// if (fitnessCalculator.isBetterFitness(choiceA.getFitness(),
		// choiceB.getFitness())) {
		// return choiceA;
		// }
		// return choiceB;
		return tournamentSelect(TOURNAMENT_NUMBER);
//		return overSelect();
	}

	private Chromosome tournamentSelect(int tournamentSize) {
		Chromosome bestChoice = population.get(r.nextInt(POPULATION_NUMBER));
		for (int i = 0; i < tournamentSize - 1; i++) {
			Chromosome newChoice = population.get(r.nextInt(POPULATION_NUMBER));
			if (fitnessCalculator.isBetterFitness(newChoice.getFitness(),
					bestChoice.getFitness())) {
				bestChoice = newChoice;
			}
		}
		return bestChoice;
	}
	
	private Chromosome overSelect(){
		if (r.nextDouble() < 0.8d){
			return(population.get(r.nextInt(320)));
		}
		else{
			return(population.get(320 + r.nextInt(POPULATION_NUMBER - 320)));
		}
	}

	/**
	 * Adds offspring to the population and it removes the worst chromosome. The
	 * worst chromosome from the population is considered the one that has the
	 * biggest fitness
	 */
//	 public void addChromosome(Chromosome offspring) {
//	 population.remove(worst);
//	 population.add(offspring);
//	 }

	public void addChromosome(Chromosome offspring) {
		population.remove(population.size() - 1);
//		if (fitnessCalculator.isBetterFitness(offspring.getFitness(), population.get(0).getFitness())){
//			population.add(0, offspring);
//			return;
//		}
//		for (int i = 0; i < population.size() - 1; i++) {
//			if (population.get(i).getFitness() >= offspring.getFitness()
//					&& offspring.getFitness() > population.get(i + 1)
//							.getFitness()) {
//				population.add(i+1, offspring);
//				return;
//			}
//		}
		int index = Collections.binarySearch(population, offspring);
		if (index < 0){
			index = ~index;
		}
		population.add(index, offspring);
	}

	/**
	 * Returns true if offspring is better than the worst chromosome from the
	 * population. The worst chromosome from the population is considered the
	 * one that has the biggest fitness
	 */
//	 public boolean chromosomeIsWorthy(Chromosome offspring) {
//		 return fitnessCalculator.isBetterFitness(offspring.getFitness(), worst.getFitness());
//	 }
	public boolean chromosomeIsWorthy(Chromosome offspring) {
		return fitnessCalculator.isBetterFitness(offspring.getFitness(),
				population.get(population.size() - 1).getFitness());
	}

	/**
	 * Returns the best chromosome from the population (the one with the
	 * smallest fitness)
	 */	
//	public Chromosome getBestChromosome() {
//		return best;
//	}
	public Chromosome getBestChromosome() {
		return population.get(0);
	}

	/**
	 * Sets the fitness for each of the chromosomes in the population.
	 * 
	 * @param radiographies
	 *            The list of radiographies.
	 */
	public void setPopulationFitness(List<Radiography> radiographies) {
		for (Chromosome c : population) {
			setFitnessToChromosome(c, radiographies);
		}
		Collections.sort(population);
	}

	public void logPopulation() {
		System.out.println(population);
		// try (PrintWriter out = new PrintWriter(new BufferedWriter(
		// new FileWriter("output.txt", true)))) {
		// out.println(population);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// for (Chromosome c : population){
		// c.getString();
		// }
	}

	/**
	 * Sets the fitness of the given choromosome, based on based on his
	 * performance on the radiographies list
	 * 
	 * @param chromosome
	 *            The chromosome to which the fitness wil be set.
	 * @param radiographies
	 *            The list of radiographies.
	 */
	public void setFitnessToChromosome(Chromosome chromosome,
			List<Radiography> radiographies) {
		chromosome.setFitness(fitnessCalculator.computeFitness(chromosome,
				radiographies));
	}

	public List<Chromosome> getChromosomes() {
		return this.population;
	}

}
