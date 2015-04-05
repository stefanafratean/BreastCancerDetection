package learning.ensembleclassifiers;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.Radiography;
import repository.RadiographyRepository;
import results.WrongEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoteEnsembleClassifier implements EnsembleClassifier {
    private RadiographyRepository radiographyRepository;
    private ChromosomeOutputComputer outputComputer;

    public VoteEnsembleClassifier(RadiographyRepository radiographyRepository, ChromosomeOutputComputer outputComputer) {
        this.radiographyRepository = radiographyRepository;
        this.outputComputer = outputComputer;
    }

    @Override
    public WrongEntry classify(List<Chromosome> paretoFrontChromosomes) {
        int negativeClassSize = getNegativeClassSize();

        int wrongCancer = 0;
        int wrongNormal = 0;

        for (Radiography r : radiographyRepository.getTestRadiographies()) {

            boolean withCancer = getParetoDecision(paretoFrontChromosomes, negativeClassSize, r);

            if (withCancer != r.isWithCancer()) {
                if (withCancer) {
                    wrongCancer++;
                } else {
                    wrongNormal++;
                }
            }
        }

        return new WrongEntry(wrongCancer, wrongNormal);
    }


    private boolean getParetoDecision(List<Chromosome> paretoFrontChromosomes, int negativeClassSize, Radiography radiography) {
        //TODO this method should globally take into account all objectives, using strategy pattern

        int cancerCount = 0;
        int normalCount = 0;
        for (Chromosome chromosome : paretoFrontChromosomes) {
            double output = outputComputer.getOutputValue(chromosome, radiography);
            if (output > getWmwInitialOutputs(chromosome).get(negativeClassSize)) {
                cancerCount++;
            } else {
                normalCount++;
            }

            // acc decision
//            if (output > 0) {
//                cancerCount++;
//            } else {
//                normalCount++;
//            }
        }

        return cancerCount > normalCount;
    }

    private List<Double> getWmwInitialOutputs(Chromosome chromosome) {
        List<Double> outputs = new ArrayList<Double>();
        for (Radiography rad : radiographyRepository.getTrainRadiographies()) {
            outputs.add(outputComputer.getOutputValue(chromosome, rad));
        }
        Collections.sort(outputs);
        return outputs;
    }

    private int getNegativeClassSize() {
        int negativeClassSize = 0;
        for (Radiography rad : radiographyRepository.getTrainRadiographies()) {
            if (!rad.isWithCancer()) {
                negativeClassSize++;
            }
        }
        return negativeClassSize;
    }
}
