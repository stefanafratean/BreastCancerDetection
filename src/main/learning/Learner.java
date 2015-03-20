package learning;

import fitness.PerformanceCalculator;
import model.Chromosome;
import model.objective.HeightObjective;
import model.objective.Objective;
import model.objective.WmwObjective;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Learner {
    private static final int GENERATIONS_NUMBER = 100;

    private final Random r;
    private final ChromosomeRepository chromosomeRepository;
    private final RadiographyRepository radiographyRepository;
    private final List<PerformanceCalculator> calculators;
    private ChromosomeOperator chromosomeOperator;
    private List<Objective> objectives;

    public Learner(List<PerformanceCalculator> calculators, ChromosomeRepository chromosomeRepository,
                   RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, Random r) {
        this.chromosomeRepository = chromosomeRepository;
        this.radiographyRepository = radiographyRepository;
        this.chromosomeOperator = chromosomeOperator;
        objectives = new ArrayList<Objective>();
        objectives.add(new WmwObjective());
        objectives.add(new HeightObjective());
//        objectives.add(new AccObjective());
//        objectives = Arrays.asList(new WmwObjective(), new AccObjective(), new HeightObjective());
        this.calculators = calculators;
        this.r = r;
    }

    public List<Chromosome> findParetoFront() {
        chromosomeRepository.setPopulationPerformanceAndFitness(radiographyRepository
                .getTrainRadiographies(), objectives);

        for (int g = 0; g < GENERATIONS_NUMBER; g++) {
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
        offspring = chromosomeOperator.xo(calculators, mother, father, r);
        if (r.nextDouble() > 0.9) {
            offspring = chromosomeOperator.mutation(offspring, r);
        }
        return offspring;
    }

    private void addChromosome(Chromosome offspring) {
        chromosomeRepository.addChromosome(offspring);
    }
}
