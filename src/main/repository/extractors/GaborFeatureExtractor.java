package repository.extractors;

import util.Files;

public class GaborFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 60;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getGaborFile();
    }

}
