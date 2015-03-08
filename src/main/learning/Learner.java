package learning;

import model.Chromosome;
import model.objective.HeightObjective;
import model.objective.Objective;
import model.objective.WmwObjective;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Learner {
    private static final int GENERATIONS_NUMBER = 10;

    private final Random r;
    private final ChromosomeRepository chromosomeRepository;
    private final RadiographyRepository radiographyRepository;
    private boolean changed = false;
    private ChromosomeOperator chromosomeOperator;
    private List<Objective> objectives;

    public Learner(ChromosomeRepository chromosomeRepository,
                   RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, Random r) {
        this.chromosomeRepository = chromosomeRepository;
        this.radiographyRepository = radiographyRepository;
        this.chromosomeOperator = chromosomeOperator;
        objectives = Arrays.asList(new WmwObjective(), new HeightObjective());
        this.r = r;
    }

    public List<Chromosome> findParetoFront() {
        chromosomeRepository.setPopulationFitness(radiographyRepository
                .getTrainRadiographies(), objectives);

        int notChangedNumber = 0;
        for (int g = 0; g < GENERATIONS_NUMBER; g++) {
            changed = false;
            for (int i = 0; i < ChromosomeRepository.POPULATION_NUMBER; i++) {
                improvePopulation();
                evaluatePopulation();
            }
            removeUnworthyDescendants();
            if (!changed) {
                notChangedNumber++;
            }
            if (notChangedNumber >= 100) {
                break;
            }
        }

        evaluateBasedOnValidationSet();

        return chromosomeRepository.getChromosomeList();
    }

    private void evaluateBasedOnValidationSet() {
        chromosomeRepository.setPopulationFitness(radiographyRepository.getValidationRadiographies(), objectives);
        chromosomeRepository.removeChromosomesThatAreWorseThanRandom();
    }

    private void removeUnworthyDescendants() {
        chromosomeRepository.removeUnworthyDescendants();
    }

    private void evaluatePopulation() {
        chromosomeRepository.evaluatePopulation(objectives);
    }

    private void improvePopulation() {
        Chromosome offspring = createNewChromosome();
        chromosomeRepository.setPerformanceMeasureToChromosome(offspring,
                radiographyRepository.getTrainRadiographies());
        addChromosome(offspring);
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

    private void addChromosomeIfWorthy(Chromosome offspring) {
        if (chromosomeRepository.chromosomeIsWorthy(offspring)) {
            chromosomeRepository.addChromosome(offspring);
            changed = true;
        }
    }
}
