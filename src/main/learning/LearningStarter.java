package learning;

import model.Chromosome;
import model.Radiography;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;
import repository.extractors.ExtractorsAggregator;
import results.ResultsProcessor;
import results.WrongEntry;
import util.Files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LearningStarter {
    private Files files;

    public LearningStarter() {
        files = new Files();
    }

    public void startLearning(ExtractorsAggregator extractors) {
        ChromosomeOperator chromosomeOperator = new ChromosomeOperator(new TerminalOperator(extractors));
        RadiographyRepository radiographyRepository = new RadiographyRepository(extractors, files.getFile());
        Random r = new Random();
        int radiographyNb = radiographyRepository.getRadiographyNb();
        int cancerNb = radiographyRepository.getCancerRadNb();
        int iter = 100;
        ResultsProcessor resProcessor = new ResultsProcessor(radiographyNb,
                cancerNb, iter);

        for (int i = 0; i < iter; i++) {
            WrongEntry wrongsPerCross = performCrossExperiment(
                    radiographyRepository, chromosomeOperator, r);
            resProcessor.computeAndShowResults(wrongsPerCross);
        }

        resProcessor.computeAndShowGlobals();
    }

    private WrongEntry performCrossExperiment(
            RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, Random r) {
        WrongEntry wrongsPerCross = new WrongEntry(0, 0);
        for (int j = 0; j < 10; j++) {
            WrongEntry wrongPerSubFold = performSubFold(radiographyRepository,
                    chromosomeOperator, r);
            wrongsPerCross.add(wrongPerSubFold);
            radiographyRepository.increaseCurrentSubset();
        }
        System.out.println("Cross exp done: " + wrongsPerCross.getTotalWrong()
                + " wrong");
        radiographyRepository.resetCurrentSubset();

        return wrongsPerCross;
    }

    private WrongEntry performSubFold(
            RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator, Random r) {
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(r, chromosomeOperator);
        Learner learner = new Learner(chromosomeRepository,
                radiographyRepository, chromosomeOperator, r);

        // CV evaluation part 2
        Chromosome best = learner.findBestChromosome();

        WrongEntry wrongEntry = classifyWmw(best,
                radiographyRepository.getTrainRadiographies(),
                radiographyRepository.getTestRadiographies(), chromosomeOperator);
        return wrongEntry;
    }

    private WrongEntry classifyWmw(Chromosome best,
                                   List<Radiography> trainRadiographies,
                                   List<Radiography> testRadiographies, ChromosomeOperator chromosomeOperator) {
        int negativeClassSize = 0;
        List<Double> outputs = new ArrayList<Double>();
        for (Radiography rad : trainRadiographies) {
            if (!rad.isWithCancer()) {
                negativeClassSize++;
            }
            outputs.add(chromosomeOperator.getOutputValue(best, rad));
        }
        Collections.sort(outputs);

        int wrongCancer = 0;
        int wrongNormal = 0;
        // rad startLearning
        for (Radiography r : testRadiographies) {
            double output = chromosomeOperator.getOutputValue(best, r);
            boolean withCancer = false;
            if (output > outputs.get(negativeClassSize)) {
                withCancer = true;
            }
            if (withCancer != r.isWithCancer()) {
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
