package repository;

import model.Chromosome;
import model.performancemeasure.PerformanceMeasure;
import org.junit.Test;
import util.Tree;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ChromosomesSorterTest {
    @Test
    public void chromosomes_are_properly_sorted() {
        Chromosome c1 = createChromosome(0, 5);
        Chromosome c2 = createChromosome(0, 6);
        Chromosome c3 = createChromosome(0, 5);
        Chromosome c4 = createChromosome(0, 5);
        Chromosome c5 = createChromosome(0, 3);
        Chromosome c6 = createChromosome(0, 1);
        List<Chromosome> chromosomeList = Arrays.asList(c1, c2, c3, c4, c5, c6);
        ChromosomesSorter sorter = new ChromosomesSorter();

        List<Chromosome> sortedList = sorter.sort(chromosomeList);

        assertTrue(sortedList.get(0) == c5);
        assertTrue(sortedList.get(1) == c6);
        assertTrue(sortedList.get(2) == c1);
        assertTrue(sortedList.get(3) == c2);
        assertTrue(sortedList.get(4) == c4);
        assertTrue(sortedList.get(5) == c3);
    }

    private Chromosome createChromosome(double fitness, int performanceValue) {
        List<PerformanceMeasure> performanceMeasures = Arrays.asList(new PerformanceMeasure(null, performanceValue));
        Chromosome chromosome = new Chromosome((Tree<Integer>) null, performanceMeasures);
        chromosome.setFitness(fitness);
        return chromosome;
    }

}