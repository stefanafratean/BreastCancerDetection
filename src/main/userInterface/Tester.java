package userInterface;

import learning.ChromosomeOperator;
import learning.Learner;
import model.Chromosome;
import model.Radiography;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;
import repository.extractors.*;
import results.ResultsProcessor;
import results.WrongEntry;
import util.Files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tester {
    // private final List<Radiography> testRadiographies;
    private HOGFeatureExtractor hogFeatureExtractor;
    private GLRLFeatureExtractor glrlFeatureExtractor;
    private MomentsExtractor momentsExtractor;
    private HaralickFeatureExtractor haralickExtractor;
    private GaborFeatureExtractor gaborExtractor;
    private CSSFeatureExtractor cssExtractor;
    private Files files;
    private ChromosomeOperator chromosomeOperator;

    public Tester() {
        files = new Files();
        chromosomeOperator = new ChromosomeOperator();
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
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(r, chromosomeOperator);
        Learner learner = new Learner(chromosomeRepository,
                radiographyRepository, chromosomeOperator, r);

        // CV evaluation part 2
        Chromosome best = learner.findBestChromosome();

        WrongEntry wrongEntry = classifyWmw(best,
                radiographyRepository.getTrainRadiographies(),
                radiographyRepository.getTestRadiographies(), true);
        best.getString();
        return wrongEntry;
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
            outputs.add(chromosomeOperator.getOutputValue(best, rad));
        }
        Collections.sort(outputs);

        int wrong = 0;
        int wrongCancer = 0;
        int wrongNormal = 0;
        // rad test
        for (Radiography r : testRadiographies) {
            double output = chromosomeOperator.getOutputValue(best, r);
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
            }
        }

        WrongEntry wrongs = new WrongEntry(wrongCancer, wrongNormal);

        return wrongs;
    }
}
