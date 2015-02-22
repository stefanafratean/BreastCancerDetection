package repository.extractors;

import util.Files;

public class HaralickFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 14;

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return new Files().getHaralickFile();
    }
}
