package learning.ensembleclassifiers;

import model.Chromosome;
import model.Radiography;
import repository.RadiographyRepository;
import results.WrongEntry;

import java.util.List;

public abstract class EnsembleClassifier {
    protected RadiographyRepository radiographyRepository;

    public WrongEntry classify(List<Chromosome> paretoFrontChromosomes) {
        int negativeClassSize = getNegativeClassSize();

        int wrongCancer = 0;
        int wrongNormal = 0;

        performExtraTraining(paretoFrontChromosomes, negativeClassSize);
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

    protected abstract void performExtraTraining(List<Chromosome> paretoFrontChromosomes, int negativeClassSize);

    protected abstract boolean getParetoDecision(List<Chromosome> paretoFrontChromosomes, int negativeClassSize, Radiography r);

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
