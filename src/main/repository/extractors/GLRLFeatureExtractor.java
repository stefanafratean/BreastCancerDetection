package repository.extractors;

import util.File;

public class GLRLFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 11;
    private File file;

    public GLRLFeatureExtractor(File file) {
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getGLRLFile();
    }
}
