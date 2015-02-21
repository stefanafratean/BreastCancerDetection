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

	public Learner(ChromosomeRepository chromosomeRepository,
			RadiographyRepository radiographyRepository, Random r) {
		this.chromosomeRepository = chromosomeRepository;
		this.radiographyRepository = radiographyRepository;
		this.r = r;
	}

	public Chromosome findBestChromosome() {
		chromosomeRepository.setPopulationFitness(radiographyRepository
				.getTrainRadiographies());
//		 chromosomeRepository.evaluatePopulation();
//		System.out.println("Done initializing population.");
		// chromosomeRepository.logPopulation();

		int notChangedNumber = 0;
		for (int g = 0; g < GENERATIONS_NUMBER; g++) {
			changed = false;
			for (int i = 0; i < ChromosomeRepository.POPULATION_NUMBER; i++) {
				improvePopulation();
			}
			if ((g + 1) % 100 == 0) {
				System.out.println(g);
			}
//			 chromosomeRepository.logPopulation();
			if (!changed) {
				notChangedNumber++;
			}
			if (notChangedNumber >= 100) {
				System.out.println("Generatii rulate: " + g);
				break;
			}
		}
		System.out.println("Fin!"
				+ chromosomeRepository.getBestChromosome().getFitness());
//		if (chromosomeRepository.getBestChromosome().getFitness() < 0.50d) {
//			chromosomeRepository.logPopulation();
//		}
		
//		chromosomeRepository.setPopulationFitness(radiographyRepository.getValidationRadiographies());
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
		offspring = ChromosomeOperator.xo(mother, father, r);
		if (r.nextDouble() > 0.9) {
			offspring = ChromosomeOperator.mutation(offspring, r);
		}
		return offspring;
	}

	private void addChromosomeIfWorthy(Chromosome offspring) {
		if (chromosomeRepository.chromosomeIsWorthy(offspring)) {
			chromosomeRepository.addChromosome(offspring);
			changed = true;
			// chromosomeRepository.evaluatePopulation();
		}
	}
}
