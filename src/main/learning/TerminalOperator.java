package learning;

import model.Radiography;
import repository.extractors.ExtractorsAggregator;
import repository.extractors.FeatureExtractor;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TerminalOperator {
    private List<Integer> constants = Arrays.asList(1, 2, 3, 4, 5, 6, 7,
            8, 9, -1, -2, -3, -4, -5, -6, -7, -8, -9);
    private int numberOfTerminals;

    //TODO this should be removed => we can just give this number as a parameter
    public TerminalOperator(ExtractorsAggregator extractors) {
        computeNumberOfTerminals(extractors);
    }

    public TerminalOperator(int numberOfTerminals) {
        this.numberOfTerminals = numberOfTerminals;
    }

    private void computeNumberOfTerminals(ExtractorsAggregator extractors) {
        numberOfTerminals = 0;
        for (FeatureExtractor extractor : extractors.getAllExtractors()) {
            addExtractorNumberOfFeatures(extractor);
        }
    }

    private void addExtractorNumberOfFeatures(FeatureExtractor extractor) {
        if (extractor != null) {
            numberOfTerminals += extractor.getNumberOfFeatures();
        }
    }

    /*
     * returns a random value from the interval [0; noOfTerminals)
     */
    public int generateTerminal(Random r) {
        return r.nextInt(numberOfTerminals);
    }

    public boolean shouldAddTerminal(Random r, boolean isFull) {
        return !isFull && r.nextDouble() < 0.5d;
    }

    /**
     * Return the terminal value corresponding to index
     *
     * @param radiography the radiography that contains terminal values for each image
     *                    descriptor
     * @param index       the index for which we retrieve the value
     * @return
     */
    public double getMappedValue(Radiography radiography, Integer index) {
        int cumulatedSize = 0;

        List<Double> descriptors = radiography.getFeatures();
        if (index < descriptors.size()) {
            return descriptors.get(index - cumulatedSize);
        }
        cumulatedSize += descriptors.size();

        double d = 0;
        try {
            d = constants.get(index - cumulatedSize);

        } catch (Exception e) {
            System.err.println("TerminalOperator: " + e.getMessage());
            e.printStackTrace();
        }
        return d;
    }

    public int getNumberOfTerminals() {
        return numberOfTerminals;
    }

}
