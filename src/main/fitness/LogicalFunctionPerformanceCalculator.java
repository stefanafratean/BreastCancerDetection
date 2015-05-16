package fitness;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.ConfusionValuesWrapper;
import model.Radiography;
import repository.FitnessHelper;

import java.util.List;

public class LogicalFunctionPerformanceCalculator implements PerformanceCalculator {
    private ChromosomeOutputComputer outputComputer;

    public LogicalFunctionPerformanceCalculator(ChromosomeOutputComputer outputComputer) {
        this.outputComputer = outputComputer;
    }

        @Override
    public double computePerformanceMeasure(Chromosome chromosome,
                                            List<Radiography> radiographies) {
        double fitness = 0;
        for (Radiography r : radiographies) {
            double chromosomeOutput = outputComputer.getOutputValue(
                    chromosome, r);
            if (madeRightDecision(r, chromosomeOutput)) {
                fitness++;
            }
        }
        return fitness;
    }

    @Override
    public boolean hasBetterPerformance(double performance1, double performance2) {
        return FitnessHelper.fitnessHasBiggerValue(performance1, performance2);

    }

    private static boolean madeRightDecision(Radiography r,
                                             double chromosomeOutput) {
        return (chromosomeOutput == 0 && !r.isWithCancer())
                || (chromosomeOutput == 1 && r.isWithCancer());
    }
//    @Override
//    public double computePerformanceMeasure(Chromosome chromosome,
//                                            List<Radiography> radiographies) {
//        ConfusionValuesWrapper confusionValues = new ConfusionValuesWrapper();
//        for (Radiography r : radiographies) {
//            double chromosomeOutput = outputComputer.getOutputValue(
//                    chromosome, r);
//            predictValue(confusionValues,
//                    isPositivePrediction(chromosomeOutput),
//                    r.isWithCancer());
//        }
//        return confusionValues.getOverallAccuracy();
//    }
//
//    private boolean isPositivePrediction(double chromosomeOutput) {
//        return chromosomeOutput == 1;
//    }
//
//    private void predictValue(ConfusionValuesWrapper confusionValues,
//                              boolean predictedPositive, boolean actualPositive) {
//        if (predictedPositive && actualPositive) {
//            confusionValues.increaseTP();
//        } else if (predictedPositive && !actualPositive) {
//            confusionValues.increaseFP();
//        } else if (!predictedPositive && !actualPositive) {
//            confusionValues.increaseTN();
//        } else {
//            confusionValues.increaseFN();
//        }
//    }
//
//    @Override
//    public boolean hasBetterPerformance(double performance1, double performance2) {
//        return  FitnessHelper.fitnessHasBiggerValue(performance1, performance2);
//    }
}
