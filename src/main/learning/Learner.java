package learning;

import model.Chromosome;
import model.objective.Objective;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Learner {
    private int generationsNumber;

    private final Random r;
    private final ChromosomeRepository chromosomeRepository;
    private final RadiographyRepository radiographyRepository;
    private ChromosomeOperator chromosomeOperator;
    private List<Objective> objectives;

    public Learner(ChromosomeRepository chromosomeRepository,
                   RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, List<Objective> objectives, Random r, int generationsNumber) {
        this.chromosomeRepository = chromosomeRepository;
        this.radiographyRepository = radiographyRepository;
        this.chromosomeOperator = chromosomeOperator;
        this.objectives = objectives;
        this.r = r;
        this.generationsNumber = generationsNumber;
    }

    public List<Chromosome> findParetoFront() {
        chromosomeRepository.setPopulationPerformanceAndFitness(radiographyRepository
                .getTrainRadiographies(), objectives);

        int b = 5;
        for (int g = 0; g < generationsNumber; g++) {
            List<Chromosome> newDescendants = new ArrayList<Chromosome>();
            for (int i = 0; i < ChromosomeRepository.POPULATION_NUMBER; i++) {
//                improvePopulation();
                newDescendants.add(createEvaluatedChromosome());
            }
            chromosomeRepository.addNewGeneration(newDescendants);
            evaluatePopulation();
            removeUnworthyDescendants();
        }

        evaluateBasedOnValidationSet();

        return chromosomeRepository.getChromosomeList();
//     return Arrays.asList(chromosomeRepository.getChromosomeList().get(0));
    }

    private void evaluateBasedOnValidationSet() {
        chromosomeRepository.setPopulationPerformanceAndFitness(radiographyRepository.getValidationRadiographies(), objectives);
//        chromosomeRepository.removeChromosomesThatAreWorseThanRandom();
        chromosomeRepository.removeChromosomesThatAreNotFromFront();
    }

    private void removeUnworthyDescendants() {
        chromosomeRepository.removeUnworthyDescendants();
    }

    private void evaluatePopulation() {
        chromosomeRepository.evaluatePopulation(objectives);
    }

    private void improvePopulation() {
        Chromosome offspring = createEvaluatedChromosome();
        addChromosome(offspring);
    }

    private Chromosome createEvaluatedChromosome() {
        Chromosome offspring = createNewChromosome();
        chromosomeRepository.setPerformanceMeasureToChromosome(offspring,
                radiographyRepository.getTrainRadiographies());
        return offspring;
    }

    private Chromosome createNewChromosome() {
        Chromosome offspring;
        Chromosome mother = chromosomeRepository.selectParent();
        Chromosome father = chromosomeRepository.selectParent();
        offspring = chromosomeOperator.xo(mother, father, r);
        if (r.nextDouble() > 0.9) {
            offspring = chromosomeOperator.mutation(offspring, r);
        }
        return offspring;
    }

    private void addChromosome(Chromosome offspring) {
        chromosomeRepository.addChromosome(offspring);
    }
}
