package learning;

import model.Chromosome;
import model.Radiography;
import repository.ChromosomeRepository;
import repository.RadiographyRepository;
import results.ResultsProcessor;
import results.WrongEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LearningStarter {
    private RadiographyRepository radiographyRepository;
    private ChromosomeOperator chromosomeOperator;

    public LearningStarter(RadiographyRepository radiographyRepository, ChromosomeOperator chromosomeOperator) {
        this.radiographyRepository = radiographyRepository;
        this.chromosomeOperator = chromosomeOperator;
    }

    public void startLearning() {
        Random r = new Random();
        int radiographyNb = radiographyRepository.getRadiographyNb();
        int cancerNb = radiographyRepository.getCancerRadNb();
        int iterations = 100;
        ResultsProcessor resProcessor = new ResultsProcessor(radiographyNb,
                cancerNb, iterations);

        for (int i = 0; i < iterations; i++) {
            WrongEntry wrongsPerCross = performCrossExperiment(r);
            resProcessor.computeAndShowResults(wrongsPerCross);
        }

        resProcessor.computeAndShowGlobals();
    }

    private WrongEntry performCrossExperiment(Random r) {
        WrongEntry wrongsPerCross = new WrongEntry(0, 0);
        for (int j = 0; j < 10; j++) {
            WrongEntry wrongPerSubFold = performSubFold(r);
            wrongsPerCross.add(wrongPerSubFold);
            radiographyRepository.increaseCurrentSubset();
        }
        System.out.println("Cross exp done: " + wrongsPerCross.getTotalWrong()
                + " wrong");
        radiographyRepository.resetCurrentSubset();

        return wrongsPerCross;
    }

    private WrongEntry performSubFold(Random r) {
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(r, chromosomeOperator);
        Learner learner = new Learner(chromosomeRepository,
                radiographyRepository, chromosomeOperator, r);

        // CV evaluation part 2
        Chromosome best = learner.findBestChromosome();

        WrongEntry wrongEntry = classifyWmw(best);
        return wrongEntry;
    }

    private WrongEntry classifyWmw(Chromosome best) {
        int negativeClassSize = 0;
        List<Double> outputs = new ArrayList<Double>();
        for (Radiography rad : radiographyRepository.getTrainRadiographies()) {
            if (!rad.isWithCancer()) {
                negativeClassSize++;
            }
            outputs.add(chromosomeOperator.getOutputValue(best, rad));
        }
        Collections.sort(outputs);

        int wrongCancer = 0;
        int wrongNormal = 0;
        // rad startLearning
        for (Radiography r : radiographyRepository.getTestRadiographies()) {
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
