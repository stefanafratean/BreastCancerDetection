package repository.extractors;

import util.File;

public class HOGFeatureExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 3780;
    private File file;

    public HOGFeatureExtractor(File file) {
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getHOGFile();
    }

}
