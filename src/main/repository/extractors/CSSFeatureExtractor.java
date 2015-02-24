package repository.extractors;

import util.File;

public class CSSFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 7781;
    private File file;

    public CSSFeatureExtractor(File file) {
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getCSSFile();
    }
}
