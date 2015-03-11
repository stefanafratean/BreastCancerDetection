package repository;


import model.Chromosome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class CrowdingDistanceAwareComparator implements Comparator<Chromosome> {
    private CrowdingDistanceComputer crowdingComputer;
    private List<Chromosome> chromosomesList;

    public CrowdingDistanceAwareComparator(List<Chromosome> chromosomeList) {
        crowdingComputer = new CrowdingDistanceComputer();
        this.chromosomesList = chromosomeList;
    }

    @Override
    public int compare(Chromosome chromosome1, Chromosome chromosome2) {
        if (chromosome1.compareTo(chromosome2) == 0) {
            return (-1) * getCrowdingDistance(chromosome1).compareTo(getCrowdingDistance(chromosome2));
        }
        return chromosome1.compareTo(chromosome2);
    }

    private Double getCrowdingDistance(Chromosome chromosome) {
        //TODO
        // 1. creare lista cu toti cromozomii cu acelasi fitnes ca si chromosome
        // 2. ordonare dupa una din axe => OX (corespunde la un obiectiv -> ordonare dupa unul din obiective)
        // 3. calculare distanta in fct de stg, dr
        // 4. corectare si la tournament selection

        //TODO asta se poate muta la exterior, si face o singura data pt cr cu acelasi fitness
        List<Chromosome> equalChromosomes = new ArrayList<Chromosome>();
        for (Chromosome c : chromosomesList) {
            if (c.compareTo(chromosome) == 0) {
                equalChromosomes.add(c);
            }
        }

        Collections.sort(equalChromosomes, new OneObjectiveComparator());
        //

        //3. Trebuie sa calculam in fct de stg si dr din lista equalChromosomes
        for (int i = 0; i < equalChromosomes.size(); i++) {
            if (chromosome == equalChromosomes.get(i)) {
                return crowdingComputer.getCrowdingDistance(equalChromosomes, i);
            }
        }
        return 0d;


        //4. putem chema direct metoda asta???
    }

    private static class OneObjectiveComparator implements Comparator<Chromosome> {
        @Override
        public int compare(Chromosome c1, Chromosome c2) {
            Double valueFromC1 = c1.getPerformanceMeasures().get(0).getValue();
            Double valueFromC2 = c2.getPerformanceMeasures().get(0).getValue();

            return valueFromC1.compareTo(valueFromC2);
        }
    }
}
