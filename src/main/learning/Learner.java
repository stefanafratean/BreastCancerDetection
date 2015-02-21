package learning;

import java.util.Random;

import model.Chromosome;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;

public class Learner {
	private static final int GENERATIONS_NUMBER = 500;

	private final Random r;
	private final ChromosomeRepository chromosomeRepository;
	private final RadiographyRepository radiographyRepository;
	private boolean changed = false;
	private ChromosomeOperator chromosomeOperator;

	public Learner(ChromosomeRepository chromosomeRepository,
				   RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, Random r) {
		this.chromosomeRepository = chromosomeRepository;
		this.radiographyRepository = radiographyRepository;
		this.chromosomeOperator = chromosomeOperator;
		this.r = r;
	}

	public Chromosome findBestChromosome() {
		chromosomeRepository.setPopulationFitness(radiographyRepository
				.getTrainRadiographies());

		int notChangedNumber = 0;
		for (int g = 0; g < GENERATIONS_NUMBER; g++) {
			changed = false;
			for (int i = 0; i < ChromosomeRepository.POPULATION_NUMBER; i++) {
				improvePopulation();
			}
			if (!changed) {
				notChangedNumber++;
			}
			if (notChangedNumber >= 100) {
				break;
			}
		}
		
		chromosomeRepository.setPopulationFitness(radiographyRepository.getValidationRadiographies());
		return chromosomeRepository.getBestChromosome();
	}

	private void improvePopulation() {
		if (r.nextDouble() < 0.9d) {

		}
		Chromosome offspring = createNewChromosome();
		chromosomeRepository.setFitnessToChromosome(offspring,
				radiographyRepository.getTrainRadiographies());
		addChromosomeIfWorthy(offspring);
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

	private void addChromosomeIfWorthy(Chromosome offspring) {
		if (chromosomeRepository.chromosomeIsWorthy(offspring)) {
			chromosomeRepository.addChromosome(offspring);
			changed = true;
		}
	}
}
