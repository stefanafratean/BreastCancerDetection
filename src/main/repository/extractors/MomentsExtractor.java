package repository.extractors;

import util.File;

public class MomentsExtractor implements FeatureExtractor {
    private final static int ELEMENTS_NUMBER = 6;
    private File file;

    public MomentsExtractor(File file){
        this.file = file;
    }

    @Override
    public int getNumberOfFeatures() {
        return ELEMENTS_NUMBER;
    }

    @Override
    public String getFileToExtractFrom() {
        return file.getMomentsFile();
    }

}
