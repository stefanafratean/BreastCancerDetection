package repository;


import model.Chromosome;

import java.util.Collections;
import java.util.List;

public class ChromosomesSorter {
    public List<Chromosome> sort(List<Chromosome> chromosomes) {
        Collections.sort(chromosomes, new CrowdingDistanceAwareComparator(chromosomes));
        return chromosomes;
    }
}
