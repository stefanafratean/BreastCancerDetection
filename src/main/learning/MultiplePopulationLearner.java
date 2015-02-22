package learning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import model.Chromosome;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;

public class MultiplePopulationLearner {
	private int subPopulationsNo = 5;
	private List<ChromosomeRepository> subPopulations;
	private RadiographyRepository radiographyRepository;
	private Random r;
	private ChromosomeOperator chromosomeOperator;

	public MultiplePopulationLearner(
			RadiographyRepository radiographyRepository, Random r, ChromosomeOperator chromosomeOperator) {
		this.chromosomeOperator = chromosomeOperator;
		subPopulations = new ArrayList<ChromosomeRepository>();
		this.r = r;
		this.radiographyRepository = radiographyRepository;
	}

	public Chromosome getBestChromosome() {
		initializeSubPopulations();

		int epochsBeforeChange = 4;
		for (int i = 0; i < epochsBeforeChange; i++) {
//			System.out.println();
//			System.out.println();
//			System.out.println("Epoch: " + i);
			List<Chromosome> bestOfAllPop = performEpoch();
			for (int j = 0; j < subPopulations.size(); j++) {
//				System.out.println("Subpop: " + j);
//				subPopulations.get(j).logPopulation();
				addChromosomeFromAnotherSubPop(bestOfAllPop, j);
			}
		}

		List<Chromosome> finalBests = new ArrayList<Chromosome>();
		for (ChromosomeRepository subPopulation : subPopulations) {
			subPopulation.setPopulationFitness(radiographyRepository
					.getValidationRadiographies());
			finalBests.add(subPopulation.getBestChromosome());
		}
		Collections.sort(finalBests);
		return finalBests.get(0);
	}

	private void addChromosomeFromAnotherSubPop(List<Chromosome> bestOfAllPop,
			int currentPop) {
		ChromosomeRepository currentSubPop = subPopulations.get(currentPop);
		int index = getChangePopulationIndex(currentPop);
        Chromosome importedChromosome = null;
        try {
            importedChromosome = bestOfAllPop.get(index).clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (currentSubPop.chromosomeIsWorthy(importedChromosome)) {
			currentSubPop.addChromosome(importedChromosome);
		}
	}

	private int getChangePopulationIndex(int j) {
		int changePopulationIndex = r.nextInt(subPopulationsNo);
		if (indexPointsToSelf(j, changePopulationIndex)) {
			findNextSuitableIndex(j, changePopulationIndex);
		}
		return changePopulationIndex;
	}

	private void findNextSuitableIndex(int j, int changePopulationIndex) {
		if (isLastPopulationInList(j)) {
			changePopulationIndex = 0;
		} else {
			changePopulationIndex++;
		}
	}

	private boolean isLastPopulationInList(int j) {
		return j == subPopulations.size() - 1;
	}

	private boolean indexPointsToSelf(int j, int changePopulationIndex) {
		return j == changePopulationIndex;
	}

	private List<Chromosome> performEpoch() {
		List<Chromosome> bestPerEpoch = new ArrayList<Chromosome>();
		for (ChromosomeRepository subPopulation : subPopulations) {
			Learner learner = new Learner(subPopulation, radiographyRepository,
					chromosomeOperator, r);
			bestPerEpoch.add(learner.findBestChromosome());
		}

		return bestPerEpoch;
	}

	private void initializeSubPopulations() {
		for (int i = 0; i < subPopulationsNo; i++) {
			subPopulations.add(new ChromosomeRepository(r, chromosomeOperator));
		}
	}

}
