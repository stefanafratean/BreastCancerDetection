package fitness;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

import java.util.ArrayList;
import java.util.List;

public class WmwNegativePerformanceCalculator implements PerformanceCalculator {
    private ChromosomeOutputComputer outputComputer;

    public WmwNegativePerformanceCalculator(ChromosomeOutputComputer outputComputer) {
        this.outputComputer = outputComputer;
    }

    @Override
    public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies) {
        List<Double> positiveClassOutputs = new ArrayList<Double>();
        List<Double> negativeClassOutputs = new ArrayList<Double>();

        for (Radiography rad : radiographies) {
            double output = outputComputer.getOutputValue(chromosome, rad);
            if (rad.isWithCancer()) {
                positiveClassOutputs.add(output);
            } else {
                negativeClassOutputs.add(output);
            }
        }

        double sum = 0;
        for (Double negativeClassOutput : negativeClassOutputs) {
            for (Double positiveClassOutput : positiveClassOutputs) {
                if (negativeClassOutput < positiveClassOutput) {
                    sum++;
                }
            }
        }

        return sum / (double) (positiveClassOutputs.size() * negativeClassOutputs.size());
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        //TODO check if we can use this and get rid of Objective class?
        return FitnessHelper.fitnessHasBiggerValue(performance1, performance2);
    }
}
