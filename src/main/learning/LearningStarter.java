package learning;

import fitness.AccPerformanceCalculator;
import fitness.HeightPerformanceCalculator;
import fitness.PerformanceCalculator;
import fitness.WmwPerformanceCalculator;
import learning.ensembleclassifiers.EnsembleClassifier;
import learning.ensembleclassifiers.VoteEnsembleClassifier;
import model.Chromosome;
import model.Radiography;
import repository.ChromosomeRepository;
import repository.FitnessHelper;
import repository.PopulationBuilder;
import repository.RadiographyRepository;
import repository.extractors.ExtractorsAggregator;
import results.ResultsProcessor;
import results.WrongEntry;

import java.util.*;

public class LearningStarter {
    private RadiographyRepository radiographyRepository;
    private TerminalOperator terminalOperator;
    private ChromosomeOutputComputer outputComputer;

    public LearningStarter(RadiographyRepository radiographyRepository, ExtractorsAggregator extractors) {
        this.radiographyRepository = radiographyRepository;
        terminalOperator = new TerminalOperator(extractors);
        outputComputer = new ChromosomeOutputComputer(terminalOperator);
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
        PerformanceCalculator wmwPerformanceCalculator = new WmwPerformanceCalculator(outputComputer);
        PerformanceCalculator accPerformanceCalculator = new AccPerformanceCalculator(outputComputer);
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
        ChromosomeOperator chromosomeOperator = new ChromosomeOperator(terminalOperator, calculators);
        PopulationBuilder builder = new PopulationBuilder(chromosomeOperator, r);
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(builder, r);
        Learner learner = new Learner(calculators, chromosomeRepository,
                radiographyRepository, chromosomeOperator, r);

        List<Chromosome> paretoFront = learner.findParetoFront();
        EnsembleClassifier ensembleClassifier = new VoteEnsembleClassifier(radiographyRepository, outputComputer);

        WrongEntry wrongEntry = ensembleClassifier.classify(paretoFront);
        return wrongEntry;
    }

}
