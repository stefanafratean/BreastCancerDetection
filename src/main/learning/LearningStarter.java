package learning;

import fitness.*;
import learning.ensembleclassifiers.CompositeEnsembleClassifier;
import learning.ensembleclassifiers.EnsembleClassifier;
import learning.ensembleclassifiers.VoteEnsembleClassifier;
import model.Chromosome;
import model.functions.*;
import model.objective.HeightObjective;
import model.objective.Objective;
import model.objective.WmwNegativeObjective;
import model.objective.WmwObjective;
import repository.ChromosomeRepository;
import repository.PopulationBuilder;
import repository.RadiographyRepository;
import repository.extractors.ExtractorsAggregator;
import results.ResultsProcessor;
import results.WrongEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LearningStarter {
    private RadiographyRepository radiographyRepository;
    private TerminalOperator terminalOperator;
    private ChromosomeOutputComputer outputComputer;
    private FunctionHelper functionsForRads;

    public LearningStarter(RadiographyRepository radiographyRepository, ExtractorsAggregator extractors) {
        this.radiographyRepository = radiographyRepository;
        terminalOperator = new TerminalOperator(extractors);
        functionsForRads = new FunctionHelper(Arrays.<Function>asList(new Plus(), new Minus(),
                new Multiplier(), new SafeDivision()));
        outputComputer = new ChromosomeOutputComputer(terminalOperator, functionsForRads);
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
        PerformanceCalculator positiveAccPerformanceCalculator = new PositiveClassAccPerformanceCalculator(outputComputer);
        PerformanceCalculator negativeAccPerformanceCalculator = new NegativeClassAccPerformanceCalculator(outputComputer);
        PerformanceCalculator heightPerformanceCalculator = new HeightPerformanceCalculator();
        PerformanceCalculator wmwNegativePerformanceCalculator = new WmwNegativePerformanceCalculator(outputComputer);
        List<PerformanceCalculator> list = new ArrayList<PerformanceCalculator>();
//        list.add(positiveAccPerformanceCalculator);
//        list.add(negativeAccPerformanceCalculator);
        list.add(wmwPerformanceCalculator);
        list.add(wmwNegativePerformanceCalculator);
//        list.add(accPerformanceCalculator);
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
        ChromosomeOperator chromosomeOperator = new ChromosomeOperator(terminalOperator, functionsForRads, calculators);
        PopulationBuilder builder = new PopulationBuilder(chromosomeOperator, r);
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(builder, r);
        List<Objective> objectives = createObjectivesList();

        Learner learner = new Learner(chromosomeRepository,
                radiographyRepository, chromosomeOperator, objectives, r, 200);

        List<Chromosome> paretoFront = learner.findParetoFront();
        EnsembleClassifier ensembleClassifier = new CompositeEnsembleClassifier(radiographyRepository, outputComputer, r);
//        EnsembleClassifier ensembleClassifier = new VoteEnsembleClassifier(radiographyRepository, outputComputer);


        WrongEntry wrongEntry = ensembleClassifier.classify(paretoFront);
        return wrongEntry;
    }

    private List<Objective> createObjectivesList() {
        List<Objective> objectives = new ArrayList<Objective>();
        objectives.add(new WmwObjective());
        objectives.add(new WmwNegativeObjective());
        // one for positive class, one for negative class
//        objectives.add(new PositiveClassAccObjective());
//        objectives.add(new NegativeClassAccObjective());
        objectives.add(new HeightObjective());
        return objectives;
    }

}
