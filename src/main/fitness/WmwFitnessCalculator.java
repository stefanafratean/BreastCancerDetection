package fitness;

import learning.ChromosomeOperator;
import model.Chromosome;
import model.Radiography;
import repository.FitnessHelper;

import java.util.ArrayList;
import java.util.List;

public class WmwFitnessCalculator implements FitnessCalculator {

    private ChromosomeOperator chromosomeOperator;

    public WmwFitnessCalculator(ChromosomeOperator chromosomeOperator) {
        this.chromosomeOperator = chromosomeOperator;
    }

    @Override
    public double computeFitness(Chromosome chromosome,
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
    public boolean isBetterFitness(double fitness1, double fitness2) {
        return FitnessHelper.fitnessHasBiggerValue(fitness1, fitness2);
    }

}
