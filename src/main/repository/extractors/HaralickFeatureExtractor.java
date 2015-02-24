package repository.extractors;

import util.File;

public class HaralickFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 14;
    private File file;

    public HaralickFeatureExtractor(File file) {
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getHaralickFile();
    }
}
