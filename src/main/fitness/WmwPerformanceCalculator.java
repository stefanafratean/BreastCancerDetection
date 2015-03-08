package fitness;

import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

import java.util.ArrayList;
import java.util.List;

public class WmwPerformanceCalculator implements PerformanceCalculator {

    private ChromosomeOperator chromosomeOperator;
    private static WmwPerformanceCalculator instance;

    public WmwPerformanceCalculator() {

    }

    public WmwPerformanceCalculator(ChromosomeOperator chromosomeOperator) {
        this.chromosomeOperator = chromosomeOperator;
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies) {
        List<Double> positiveClassOutputs = new ArrayList<Double>();
        List<Double> negativeClassOutputs = new ArrayList<Double>();

        for (Radiography rad : radiographies) {
            double output = chromosomeOperator.getOutputValue(chromosome, rad);
            if (rad.isWithCancer()) {
                positiveClassOutputs.add(output);
            } else {
                negativeClassOutputs.add(output);
            }
        }

        double sum = 0;
        for (Double positiveClassOutput : positiveClassOutputs) {
            for (Double negativeClassOutput : negativeClassOutputs) {
                if (positiveClassOutput > 0 && positiveClassOutput > negativeClassOutput) {
                    sum++;
                }
            }
        }

        return sum / (double) (positiveClassOutputs.size() * negativeClassOutputs.size());
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return FitnessHelper.fitnessHasBiggerValue(performance1, performance2);
    }

    public static PerformanceCalculator getInstance() {
        if (instance == null) {
            return new WmwPerformanceCalculator();
        }
        return instance;
    }
}
