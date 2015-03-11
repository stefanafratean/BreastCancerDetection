package repository;

import model.Chromosome;
import model.performancemeasure.PerformanceMeasure;
import org.junit.Test;
import util.Tree;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CrowdingDistanceAwareComparatorTest {

    @Test
    public void chromosome_in_less_crowded_area_is_better_ordered_chromosomes() {
        Chromosome c1 = mockChromosome(0, 1);
        Chromosome c2 = mockChromosome(0, 1);
        Chromosome c3 = mockChromosome(0, 2);
        Chromosome c4 = mockChromosome(0, 3);
        List<Chromosome> chromosomeList = Arrays.asList(c1, c2, c3, c4);
        CrowdingDistanceAwareComparator comparator = new CrowdingDistanceAwareComparator(chromosomeList);

        assertTrue(comparator.compare(c3, c2) < 0);
        assertTrue(comparator.compare(c3, c1) < 0);
    }

    @Test
    public void chromosome_in_less_crowded_area_is_better_unordered_chromosomes() {
        Chromosome c1 = mockChromosome(0, 5);
        Chromosome c2 = mockChromosome(0, 6);
        Chromosome c3 = mockChromosome(0, 5);
        Chromosome c4 = mockChromosome(0, 5);
        Chromosome c5 = mockChromosome(0, 3);
        Chromosome c6 = mockChromosome(0, 1);
        List<Chromosome> chromosomeList = Arrays.asList(c1, c2, c3, c4, c5, c6);
        CrowdingDistanceAwareComparator comparator = new CrowdingDistanceAwareComparator(chromosomeList);

        assertTrue(comparator.compare(c1, c3) < 0);
        assertTrue(comparator.compare(c2, c3) < 0);
        assertTrue(comparator.compare(c5, c3) < 0);
        assertTrue(comparator.compare(c6, c3) < 0);
    }

    private Chromosome mockChromosome(double fitness, int performanceValue) {
        List<PerformanceMeasure> performanceMeasures = Arrays.asList(new PerformanceMeasure(null, performanceValue));
        Chromosome chromosome = new Chromosome((Tree<Integer>) null, performanceMeasures);
        chromosome.setFitness(fitness);
        return chromosome;
    }
}