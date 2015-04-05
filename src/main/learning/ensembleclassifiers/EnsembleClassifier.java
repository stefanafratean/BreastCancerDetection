package learning.ensembleclassifiers;

import model.Chromosome;
import results.WrongEntry;

import java.util.List;

public interface EnsembleClassifier {

    WrongEntry classify(List<Chromosome> paretoFrontChromosomes);
}
