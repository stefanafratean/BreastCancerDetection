package learning.ensembleclassifiers;

import learning.ChromosomeOutputComputer;
import model.Chromosome;
import model.Radiography;
import repository.RadiographyRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VoteEnsembleClassifier extends EnsembleClassifier {
    private ChromosomeOutputComputer outputComputer;

    public VoteEnsembleClassifier(RadiographyRepository radiographyRepository, ChromosomeOutputComputer outputComputer) {
        super.radiographyRepository = radiographyRepository;
        this.outputComputer = outputComputer;
    }

    @Override
    protected void performExtraTraining(List<Chromosome> paretoFrontChromosomes, int negativeClassSize) {
        // no extra training in this case
    }

    @Override
    protected boolean getParetoDecision(List<Chromosome> paretoFrontChromosomes, int negativeClassSize, Radiography radiography) {
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

}
