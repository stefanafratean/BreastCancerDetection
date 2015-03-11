package repository;

import fitness.PerformanceCalculator;
import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;
import model.objective.Objective;
import model.performancemeasure.PerformanceMeasure;

import java.util.*;

public class ChromosomeRepository {
    public static final int POPULATION_NUMBER = 100;
    // private static final int TOURNAMENT_NUMBER = 5 / 100 * POPULATION_NUMBER;
    private static final int TOURNAMENT_NUMBER = 20;
    private final List<Chromosome> population;
    private final Random r;
    private final List<PerformanceCalculator> calculators;

    public ChromosomeRepository(List<PerformanceCalculator> calculators, Random r, ChromosomeOperator chromosomeOperator) {
        this.r = r;
        this.calculators = calculators;
        population = new ArrayList<Chromosome>();
        initPopulation(chromosomeOperator);
    }

    // initialize population with the method ramped half-half
    private void initPopulation(ChromosomeOperator chromosomeOperator) {
        for (int i = 0; i < POPULATION_NUMBER / 2; i++) {
            population.add(chromosomeOperator.createChromosome(calculators,
                    chromosomeOperator.MAX_CHROMOSOME_DEPTH, r, false));
            population.add(chromosomeOperator.createChromosome(calculators,
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
        int bestChoiceIndex = r.nextInt(POPULATION_NUMBER);
        Chromosome bestChoice = population.get(bestChoiceIndex);
        for (int i = 0; i < tournamentSize - 1; i++) {
            int newChoiceIndex = r.nextInt(POPULATION_NUMBER);
            Chromosome newChoice = population.get(newChoiceIndex);
            if (newChoice.getFitness() < bestChoice.getFitness()) {
                bestChoice = newChoice;
                bestChoiceIndex = newChoiceIndex;
                //TODO check if crowding distance supposed to be bigger or smaller
            } else if (equalFitnessInLessCrowdedArea(newChoiceIndex, bestChoiceIndex)) {
                bestChoice = newChoice;
                bestChoiceIndex = newChoiceIndex;
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

        if (doesNotExist(offspring)) {
            population.add(index, offspring);
        }
    }

    private boolean doesNotExist(Chromosome offspring) {
        for (Chromosome c : population) {
            if (offspring.equals(c)) {
                return false;
            }
        }
        return true;
    }

//    /**
//     * Returns true if offspring is better than the worst chromosome from the
//     * population. The worst chromosome from the population is considered the
//     * one that has the biggest fitness
//     */
//    public boolean chromosomeIsWorthy(Chromosome offspring) {
//        return wmwPerformanceCalculator.hasBetterPerformance(offspring.getFitness(),
//                population.get(population.size() - 1).getFitness());
//    }

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
     * @param objectives    List of objectives to be evaluated.
     */
    public void setPopulationFitness(List<Radiography> radiographies, List<Objective> objectives) {
        for (Chromosome c : population) {
            setPerformanceMeasureToChromosome(c, radiographies);
        }
        evaluatePopulation(objectives);
        sort();
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
        for (PerformanceMeasure measure : chromosome.getPerformanceMeasures()) {
            double performanceValue = measure.getPerformanceCalculator().computePerformanceMeasure(chromosome, radiographies);
            measure.setValue(performanceValue);
        }
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
        sort();
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
            for (PerformanceMeasure measure : chromosome.getPerformanceMeasures()) {
                if (measure.getValue() < 0.5d) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public void sort() {
        //TODO remove this and use the parametrized one
//        for (int i = 0; i < population.size(); i++) {
//            for (int j = i + 1; j < population.size(); j++) {
//                if (shouldChange(i, j)) {
//                    Chromosome aux = population.get(i);
//                    population.set(i, population.get(j));
//                    population.set(j, aux);
//                }
//            }
//        }
        Collections.sort(population, new CrowdingDistanceAwareComparator(population));
    }

    private boolean equalFitnessInLessCrowdedArea(int i, int j) {
        Chromosome chromosome1 = population.get(i);
        Chromosome chromosome2 = population.get(j);
        CrowdingDistanceComputer computer = new CrowdingDistanceComputer();
        return chromosome1.compareTo(chromosome2) == 0 && computer.getCrowdingDistance(population, i) > computer.getCrowdingDistance(population, j);
    }

    private boolean hasBetterFitness(int i, int j) {
        return population.get(i).compareTo(population.get(j)) > 0;
    }

    public void removeChromosomesThatAreNotFromFront() {
        while (!isNonDominatedChromosome(population.get(population.size() - 1))) {
            population.remove(population.size() - 1);
        }
    }

    private boolean isNonDominatedChromosome(Chromosome chromosome) {
        return chromosome.getFitness() == 0;
    }
}
