package repository;

import learning.ChromosomeOperator;
import model.Chromosome;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PopulationBuilderTest {

    @Test
    public void population_is_created() {
        ChromosomeOperator operator = mock(ChromosomeOperator.class);
        Chromosome chromosome = mock(Chromosome.class);
        when(operator.createChromosome(any(Random.class), anyBoolean())).thenReturn(chromosome);
        PopulationBuilder builder = new PopulationBuilder(operator, null);

        int popNb = 50;
        List<Chromosome> population = builder.createInitialPopulation(popNb);

        assertEquals(popNb, population.size());
    }

}