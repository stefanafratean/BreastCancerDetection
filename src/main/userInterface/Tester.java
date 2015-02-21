package userInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.rmi.CORBA.Util;

import learning.ChromosomeOperator;
import learning.Learner;
import learning.MultiplePopulationLearner;
import model.Chromosome;
import model.Radiography;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;
import repository.extractors.CSSFeatureExtractor;
import repository.extractors.GLRLFeatureExtractor;
import repository.extractors.GaborFeatureExtractor;
import repository.extractors.HOGFeatureExtractor;
import repository.extractors.HaralickFeatureExtractor;
import repository.extractors.MomentsExtractor;
import results.AccuracyComputer;
import results.ResultsProcessor;
import results.WrongEntry;
import util.Files;

public class Tester {
	// private final List<Radiography> testRadiographies;
	private HOGFeatureExtractor hogFeatureExtractor;
	private GLRLFeatureExtractor glrlFeatureExtractor;
	private MomentsExtractor momentsExtractor;
	private HaralickFeatureExtractor haralickExtractor;
	private GaborFeatureExtractor gaborExtractor;
	private CSSFeatureExtractor cssExtractor;
	private Files files;

	public Tester() {
		files = new Files();
		initializeExtractors();
	}

	private void initializeExtractors() {
//		 hogFeatureExtractor = new HOGFeatureExtractor();
//		 glrlFeatureExtractor = new GLRLFeatureExtractor();
		momentsExtractor = new MomentsExtractor();
//		haralickExtractor = new HaralickFeatureExtractor();
		// gaborExtractor = new GaborFeatureExtractor();
		// cssExtractor = new CSSFeatureExtractor();
	}

	public void test() {
		RadiographyRepository radiographyRepository = new RadiographyRepository(
				hogFeatureExtractor, glrlFeatureExtractor, momentsExtractor,
				haralickExtractor, gaborExtractor, cssExtractor,
				files.getFile());
		Random r = new Random();
		int radiographyNb = radiographyRepository.getRadiographyNb();
		int cancerNb = radiographyRepository.getCancerRadNo();
		int iter = 100;
		ResultsProcessor resProcessor = new ResultsProcessor(radiographyNb,
				cancerNb, iter);

		for (int i = 0; i < iter; i++) {
			WrongEntry wrongsPerCross = performCrossExperiment(
					radiographyRepository, r);
			resProcessor.computeAndShowResults(wrongsPerCross);
		}

		resProcessor.computeAndShowGlobals();
	}

	private WrongEntry performCrossExperiment(
			RadiographyRepository radiographyRepository, Random r) {
		WrongEntry wrongsPerCross = new WrongEntry(0, 0);
		for (int j = 0; j < 10; j++) {
			WrongEntry wrongPerSubFold = performSubFold(radiographyRepository,
					r);
			wrongsPerCross.add(wrongPerSubFold);
			radiographyRepository.increaseCurrentSubset();
		}
		System.out.println("Cross exp done: " + wrongsPerCross.getTotalWrong()
				+ " wrong");
		radiographyRepository.resetCurrentSubset();

		return wrongsPerCross;
	}

	private WrongEntry performSubFold(
			RadiographyRepository radiographyRepository, Random r) {
		ChromosomeRepository chromosomeRepository = new ChromosomeRepository(r);
		Learner learner = new Learner(chromosomeRepository,
				radiographyRepository, r);
		// Chromosome best = learner.findBestChromosome();

		// MultiplePopulationLearner multipleLearner = new
		// MultiplePopulationLearner(
		// radiographyRepository, r);
		// Chromosome best = multipleLearner.getBestChromosome();

		// CV evaluation part 2
		learner.findBestChromosome();
		Chromosome best = determineBestBasedOnValidationSet(
				radiographyRepository, chromosomeRepository);

		WrongEntry wrongEntry = classifyWmw(best,
				radiographyRepository.getTrainRadiographies(),
				radiographyRepository.getTestRadiographies(), true);
		best.getString();
		return wrongEntry;
	}

	private Chromosome determineBestBasedOnValidationSet(
			RadiographyRepository radiographyRepository,
			ChromosomeRepository chromosomeRepository) {
		List<Integer> wrongResults = getNbOfWrongForEachChromosome(
				radiographyRepository, chromosomeRepository);

		int min = Collections.min(wrongResults);
		Chromosome best = null;
		for (int i = 0; i < wrongResults.size(); i++) {
			if (wrongResults.get(i) == min) {
				best = chromosomeRepository.getChromosomes().get(i);
				break;
			}
		}
		return best;
	}

	private List<Integer> getNbOfWrongForEachChromosome(
			RadiographyRepository radiographyRepository,
			ChromosomeRepository chromosomeRepository) {
		List<Integer> wrongResults = new ArrayList<Integer>();
		for (Chromosome c : chromosomeRepository.getChromosomes()) {
			WrongEntry wrongRes = classifyWmw(c,
					radiographyRepository.getTrainRadiographies(),
					radiographyRepository.getValidationRadiographies(), false);
			wrongResults.add(wrongRes.getTotalWrong());
		}

		return wrongResults;
	}

	private WrongEntry classifyWmw(Chromosome best,
			List<Radiography> trainRadiographies,
			List<Radiography> testRadiographies, boolean updateTotals) {
		int negativeClassSize = 0;
		List<Double> outputs = new ArrayList<Double>();
		for (Radiography rad : trainRadiographies) {
			if (!rad.isWithCancer()) {
				negativeClassSize++;
			}
			outputs.add(ChromosomeOperator.getOuputValue(best, rad));
		}
		Collections.sort(outputs);

		int wrong = 0;
		int wrongCancer = 0;
		int wrongNormal = 0;
		// rad test
		for (Radiography r : testRadiographies) {
			double output = ChromosomeOperator.getOuputValue(best, r);
			boolean withCancer = false;
			if (output > outputs.get(negativeClassSize)) {
				withCancer = true;
			}
			if (withCancer != r.isWithCancer()) {
				wrong++;
				if (withCancer) {
					wrongCancer++;
				} else {
					wrongNormal++;
				}
				// System.out.println(r.getName());
			}
		}

		WrongEntry wrongs = new WrongEntry(wrongCancer, wrongNormal);

		if (updateTotals) {
			System.out.println("Gresit: " + wrong);
			System.out.println("Wrong cancer: " + wrongCancer);
			System.out.println("Wrong normal: " + wrongNormal);
		}
		return wrongs;
	}
}
