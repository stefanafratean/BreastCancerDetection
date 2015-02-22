package repository.extractors;

import util.Files;

public class HOGFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 3780;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getHOGFile();
    }

}
