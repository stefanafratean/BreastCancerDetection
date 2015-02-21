package repository;

import fitness.FitnessCalculator;
import fitness.WmwFitnessCalculator;
import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ChromosomeRepository {
    public static final int POPULATION_NUMBER = 100;
    // private static final int TOURNAMENT_NUMBER = 5 / 100 * POPULATION_NUMBER;
    private static final int TOURNAMENT_NUMBER = 20;
    private final List<Chromosome> population;
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
     * Randomly selects a chromosome from the population
     */
    public Chromosome selectParent() {
        return tournamentSelect(TOURNAMENT_NUMBER);
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

    private Chromosome overSelect() {
        if (r.nextDouble() < 0.8d) {
            return (population.get(r.nextInt(320)));
        } else {
            return (population.get(320 + r.nextInt(POPULATION_NUMBER - 320)));
        }
    }

    /**
     * Adds offspring to the population and it removes the worst chromosome. The
     * worst chromosome from the population is considered the one that has the
     * biggest fitness
     */
    public void addChromosome(Chromosome offspring) {
        population.remove(population.size() - 1);
        int index = Collections.binarySearch(population, offspring);
        if (index < 0) {
            index = ~index;
        }
        population.add(index, offspring);
    }

    /**
     * Returns true if offspring is better than the worst chromosome from the
     * population. The worst chromosome from the population is considered the
     * one that has the biggest fitness
     */
    public boolean chromosomeIsWorthy(Chromosome offspring) {
        return fitnessCalculator.isBetterFitness(offspring.getFitness(),
                population.get(population.size() - 1).getFitness());
    }

    /**
     * Returns the best chromosome from the population (the one with the
     * smallest fitness)
     */
    public Chromosome getBestChromosome() {
        return population.get(0);
    }

    /**
     * Sets the fitness for each of the chromosomes in the population.
     *
     * @param radiographies The list of radiographies.
     */
    public void setPopulationFitness(List<Radiography> radiographies) {
        for (Chromosome c : population) {
            setFitnessToChromosome(c, radiographies);
        }
        Collections.sort(population);
    }

    /**
     * Sets the fitness of the given choromosome, based on based on his
     * performance on the radiographies list
     *
     * @param chromosome    The chromosome to which the fitness wil be set.
     * @param radiographies The list of radiographies.
     */
    public void setFitnessToChromosome(Chromosome chromosome,
                                       List<Radiography> radiographies) {
        chromosome.setFitness(fitnessCalculator.computeFitness(chromosome,
                radiographies));
    }
}
