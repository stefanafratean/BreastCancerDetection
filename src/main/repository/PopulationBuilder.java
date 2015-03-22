package repository;

import fitness.PerformanceCalculator;
import learning.ChromosomeOperator;
import model.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationBuilder {

    private ChromosomeOperator chromosomeOperator;
    private Random r;

    public PopulationBuilder(ChromosomeOperator chromosomeOperator, Random r) {
        this.chromosomeOperator = chromosomeOperator;
        this.r = r;
    }

    // initialize population with the method ramped half-half
    public List<Chromosome> createInitialPopulation(int popNumber) {
        List<Chromosome> population = new ArrayList<Chromosome>();
        for (int i = 0; i < popNumber / 2; i++) {
            population.add(chromosomeOperator.createChromosome(r, false));
            population.add(chromosomeOperator.createChromosome(r, true));
        }

        return population;
    }
}
