package repository.extractors;

import util.Files;

public class CSSFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 7781;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getCSSFile();
    }
}
