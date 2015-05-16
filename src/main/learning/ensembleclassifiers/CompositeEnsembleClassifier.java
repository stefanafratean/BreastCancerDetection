package learning.ensembleclassifiers;

import fitness.HeightPerformanceCalculator;
import fitness.LogicalFunctionPerformanceCalculator;
import fitness.PerformanceCalculator;
import learning.ChromosomeOperator;
import learning.ChromosomeOutputComputer;
import learning.Learner;
import learning.TerminalOperator;
import model.Chromosome;
import model.Radiography;
import model.functions.*;
import model.objective.HeightObjective;
import model.objective.LogicalFunctionObjective;
import model.objective.Objective;
import repository.ChromosomeRepository;
import repository.PopulationBuilder;
import repository.RadiographyRepository;

import java.util.*;

public class CompositeEnsembleClassifier extends EnsembleClassifier {
    private final ChromosomeOutputComputer outputComputer;
    private Random r;
    private Chromosome compositeChr;
    private RadiographyRepository radRepoForMeta;
    private ChromosomeOutputComputer outputComputerForMeta;

    public CompositeEnsembleClassifier(RadiographyRepository radiographyRepository, ChromosomeOutputComputer outputComputer, Random r) {
        this.r = r;
        super.radiographyRepository = radiographyRepository;
        this.outputComputer = outputComputer;
    }

    @Override
    protected void performExtraTraining(List<Chromosome> paretoFrontChromosomes, int negativeClassSize) {
        List<Radiography> radsForMetaClassifier = getMetaInput(paretoFrontChromosomes, negativeClassSize);
        radRepoForMeta = new RadiographyRepository(radsForMetaClassifier);

        TerminalOperator terminalOperator = new TerminalOperator(paretoFrontChromosomes.size());
        FunctionHelper functionHelper = new FunctionHelper(Arrays.<Function>asList(new And(), new Or(), new Vote(), new Xor()));
        List<PerformanceCalculator> performanceCalculators = new ArrayList<PerformanceCalculator>();
        outputComputerForMeta = new ChromosomeOutputComputer(terminalOperator, functionHelper);
        performanceCalculators.add(new LogicalFunctionPerformanceCalculator(outputComputerForMeta));
        performanceCalculators.add(new HeightPerformanceCalculator());
        ChromosomeOperator chromosomeOperator = new ChromosomeOperator(terminalOperator, functionHelper, performanceCalculators);
        PopulationBuilder populationBuilder = new PopulationBuilder(chromosomeOperator, r);
        ChromosomeRepository chromosomeRepository = new ChromosomeRepository(populationBuilder, r);
        List<Objective> objectives = new ArrayList<Objective>();
        objectives.add(new LogicalFunctionObjective());
        objectives.add(new HeightObjective());
        Learner learner = new Learner(chromosomeRepository, radRepoForMeta, chromosomeOperator, objectives, r, 50);

        compositeChr = learner.findParetoFront().get(0);
    }

    private List<Radiography> getMetaInput(List<Chromosome> paretoFrontChromosomes, int negativeClassSize) {
        List<Radiography> newRadiographies = new ArrayList<Radiography>();
        for (Radiography radiography : radiographyRepository.getTrainRadiographies()) {
            Radiography newRadiography = getMetaRadiography(paretoFrontChromosomes, radiography, negativeClassSize);
            newRadiographies.add(newRadiography);
        }

        return newRadiographies;
    }

    private Radiography getMetaRadiography(List<Chromosome> paretoFrontChromosomes, Radiography radiography, int negativeClassSize) {
        Radiography newRadiography = new Radiography(radiography.isWithCancer());
        double[] outputs = computeNewDescriptors(paretoFrontChromosomes, radiography, negativeClassSize);
        newRadiography.addDescriptors(outputs);
        return newRadiography;
    }

    private double[] computeNewDescriptors(List<Chromosome> paretoFrontChromosomes, Radiography radiography, int negativeClassSize) {
        double[] outputs = new double[paretoFrontChromosomes.size()];
        int i = 0;
        for (Chromosome chromosome : paretoFrontChromosomes) {
            double outputValue = outputComputer.getOutputValue(chromosome, radiography);

            //TODO what happens for multiple objectives with output value (e.g. not height)? Consider descriptor for each value / make vote and only take into account the final decision of the pareto front chr
            if (outputValue > getWmwInitialOutputs(chromosome, radiographyRepository, outputComputer).get(negativeClassSize)) {
                outputs[i++] = 1d;
            } else {
                outputs[i++] = 0d;
            }

//            if (outputValue > 0) {
//                outputs[i++] = 1d;
//            } else {
//                outputs[i++] = 0d;
//            }
        }
        return outputs;
    }

    @Override
    protected boolean getParetoDecision(List<Chromosome> paretoFrontChromosomes, int negativeClassSize, Radiography radiography) {
        Radiography metaRadiography = getMetaRadiography(paretoFrontChromosomes, radiography, negativeClassSize);
        double output = outputComputerForMeta.getOutputValue(compositeChr, metaRadiography);
        if (output == 1) {
//        if (output > 0.5) {
            return true;
        } else if (output > 1) {
            System.err.println("CompEnsembleClass: Something went wrong, output should be 0 or 1.");
        }
        return false;
    }

    private List<Double> getWmwInitialOutputs(Chromosome chromosome, RadiographyRepository radRepo, ChromosomeOutputComputer outputComputer) {
        List<Double> outputs = new ArrayList<Double>();
        for (Radiography rad : radRepo.getTrainRadiographies()) {
            outputs.add(outputComputer.getOutputValue(chromosome, rad));
        }
        Collections.sort(outputs);
        return outputs;
    }
}
