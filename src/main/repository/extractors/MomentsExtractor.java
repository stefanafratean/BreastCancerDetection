package repository.extractors;

import util.Files;

public class MomentsExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 6;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getMomentsFile();
    }

}
