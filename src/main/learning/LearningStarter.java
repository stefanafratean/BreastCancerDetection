package learning;

import fitness.HeightPerformanceCalculator;
import fitness.PerformanceCalculator;
import fitness.WmwPerformanceCalculator;
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
        List<PerformanceCalculator> calculators = getPerformanceCalculators();

        int radiographyNb = radiographyRepository.getRadiographyNb();
        int cancerNb = radiographyRepository.getCancerRadNb();
        int iterations = 10;
        ResultsProcessor resProcessor = new ResultsProcessor(radiographyNb,
                cancerNb, iterations);

        for (int i = 0; i < iterations; i++) {
            WrongEntry wrongsPerCross = performCrossExperiment(r, calculators);
            resProcessor.computeAndShowResults(wrongsPerCross);
        }

        resProcessor.computeAndShowGlobals();
    }

    private List<PerformanceCalculator> getPerformanceCalculators() {
        PerformanceCalculator wmwPerformanceCalculator = new WmwPerformanceCalculator(chromosomeOperator);
//        PerformanceCalculator accPerformanceCalculator = new AccPerformanceCalculator(chromosomeOperator);
        PerformanceCalculator heightPerformanceCalculator = new HeightPerformanceCalculator();
//        return Arrays.asList(wmwPerformanceCalculator, accPerformanceCalculator);
        List<PerformanceCalculator> list = new ArrayList<PerformanceCalculator>();
        list.add(wmwPerformanceCalculator);
        list.add(heightPerformanceCalculator);

        return list;
    }

    private WrongEntry performCrossExperiment(Random r, List<PerformanceCalculator> calculators) {
        WrongEntry wrongsPerCross = new WrongEntry(0, 0);
        for (int j = 0; j < 10; j++) {
            WrongEntry wrongPerSubFold = performSubFold(r, calculators);
            wrongsPerCross.add(wrongPerSubFold);
            radiographyRepository.increaseCurrentSubset();
        }
        System.out.println("Cross exp done: " + wrongsPerCross.getTotalWrong()
                + " wrong");
        radiographyRepository.resetCurrentSubset();

        return wrongsPerCross;
    }

    private WrongEntry performSubFold(Random r, List<PerformanceCalculator> calculators) {

        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(calculators, r, chromosomeOperator);
        Learner learner = new Learner(calculators, chromosomeRepository,
                radiographyRepository, chromosomeOperator, r);

        // CV evaluation part 2
        List<Chromosome> paretoFront = learner.findParetoFront();

        WrongEntry wrongEntry = classifyForEachObjective(paretoFront);
        return wrongEntry;
    }

    //TODO take into account all performance measures for the classification
    private WrongEntry classifyForEachObjective(List<Chromosome> paretoFrontChromosomes) {
        List<Integer> negativeClassSizes = new ArrayList<Integer>();
        for (int i = 0; i < paretoFrontChromosomes.size(); i++) {
            negativeClassSizes.add(0);
        }
        ArrayList<ArrayList<Double>> outputs = new ArrayList<ArrayList<Double>>();
        for (int i = 0; i < paretoFrontChromosomes.size(); i++) {
            ArrayList<Double> outputForCurrentChr = new ArrayList<Double>();
            for (Radiography rad : radiographyRepository.getTrainRadiographies()) {
                if (!rad.isWithCancer()) {
                    negativeClassSizes.set(i, negativeClassSizes.get(i) + 1);
                }
                outputForCurrentChr.add(chromosomeOperator.getOutputValue(paretoFrontChromosomes.get(i), rad));
            }
            Collections.sort(outputForCurrentChr);
            outputs.add(outputForCurrentChr);
        }

        int wrongCancer = 0;
        int wrongNormal = 0;
        // rad startLearning
        for (Radiography r : radiographyRepository.getTestRadiographies()) {
            boolean withCancer = false;
            double cancerCount = 0d;
            double normalCount = 0d;
            for (int i = 0; i < paretoFrontChromosomes.size(); i++) {
                double outputForCurrentChr = chromosomeOperator.getOutputValue(paretoFrontChromosomes.get(i), r);
                //wmw decision
                ArrayList<Double> outputsForCurrentChr = outputs.get(i);
                Integer negativeClassSizeForCurrentChr = negativeClassSizes.get(i);
                double wmwWeight = paretoFrontChromosomes.get(i).getPerformanceMeasures().get(0).getValue();
                if (outputForCurrentChr > outputsForCurrentChr.get(negativeClassSizeForCurrentChr)) {
                    cancerCount++;
                } else {
                    normalCount++;
                }

                //acc decision
//                double accWeight = paretoFrontChromosomes.get(i).getPerformanceMeasures().get(1).getValue();
//                if (FitnessHelper.itHasCancer(outputForCurrentChr)) {
//                    cancerCount++;
//                } else {
//                    normalCount++;
//                }
            }
            if (cancerCount >= normalCount) {
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
