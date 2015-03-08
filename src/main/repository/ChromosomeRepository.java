package repository;

import fitness.FitnessCalculator;
import fitness.WmwFitnessCalculator;
import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;
import model.objective.Objective;

import java.util.*;

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
            if (newChoice.getFitness() < bestChoice.getFitness()) {
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
     * Adds offspring to the population.
     */
    public void addChromosome(Chromosome offspring) {
//        population.remove(population.size() - 1);
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
     * @param objectives
     */
    public void setPopulationFitness(List<Radiography> radiographies, List<Objective> objectives) {
        for (Chromosome c : population) {
            setPerformanceMeasureToChromosome(c, radiographies);
        }
//        Collections.sort(population);
        evaluatePopulation(objectives);
    }

    /**
     * Sets the fitness of the given chromosome, based on based on his
     * performance on the radiographies list
     *
     * @param chromosome    The chromosome to which the fitness wil be set.
     * @param radiographies The list of radiographies.
     */
    public void setPerformanceMeasureToChromosome(Chromosome chromosome,
                                                  List<Radiography> radiographies) {
        chromosome.setWmw(fitnessCalculator.computeFitness(chromosome,
                radiographies));
    }

    public void evaluatePopulation(List<Objective> objectives) {
        for (Chromosome chromosome : population) {
            chromosome.setFitness(computeRank(chromosome, objectives));
        }
    }

    private double computeRank(Chromosome chromosome, List<Objective> objectives) {
        int rank = 0;
        for (Chromosome otherChromosome : population) {
            // don't check for the same instance (but check for other instances with equal value)
            if (chromosome != otherChromosome) {
                if (dominates(otherChromosome, chromosome, objectives)) {
                    rank++;
                }
            }
        }
        return rank;
    }

    /*
     * Checks if chromosome1 dominates chromosome2. A chromosome c1 dominates a chromosome c2 if it as
     * least as good as c2 on all of the objectives and better on at least one
     */
    private boolean dominates(Chromosome chromosome1, Chromosome chromosome2, List<Objective> objectives) {
        boolean allAsGood = true;
        boolean oneBetter = false;
        for (Objective objective : objectives) {
            if (objective.compare(chromosome1, chromosome2) > 0) {
                oneBetter = true;
            } else if (!(objective.compare(chromosome1, chromosome2) == 0)) {
                allAsGood = false;
            }
        }
        return allAsGood && oneBetter;
    }

    public void removeUnworthyDescendants() {
        Collections.sort(population);
        while (population.size() > POPULATION_NUMBER) {
            population.remove(population.size() - 1);
        }
    }

    public List<Chromosome> getChromosomeList() {
        return population;
    }

    public void removeChromosomesThatAreWorseThanRandom() {
        Iterator<Chromosome> it = population.iterator();
        while (it.hasNext()) {
            Chromosome chromosome = it.next();
            if (chromosome.getWmw() < 0.5d) {
                it.remove();
            }
        }
    }
}
