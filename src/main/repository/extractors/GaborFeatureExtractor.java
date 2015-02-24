package repository.extractors;

import util.File;

public class GaborFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 60;
    private File file;

    public GaborFeatureExtractor(File file) {
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getGaborFile();
    }

}
