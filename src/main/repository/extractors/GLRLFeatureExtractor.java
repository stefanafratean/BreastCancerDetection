package repository.extractors;

import util.Files;

public class GLRLFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 11;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getGLRLFile();
    }
}
