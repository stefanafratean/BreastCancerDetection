package repository.extractors;

import repository.extractors.*;

public class ExtractorsAggregator {
    private final HOGFeatureExtractor hogFeatureExtractor;
    private final GLRLFeatureExtractor glrlFeatureExtractor;
    private final MomentsExtractor momentsExtractor;
    private final HaralickFeatureExtractor haralickExtractor;
    private final GaborFeatureExtractor gaborExtractor;
    private final CSSFeatureExtractor cssExtractor;

    public ExtractorsAggregator(HOGFeatureExtractor hogFeatureExtractor, GLRLFeatureExtractor glrlFeatureExtractor, MomentsExtractor momentsExtractor, HaralickFeatureExtractor haralickExtractor, GaborFeatureExtractor gaborExtractor, CSSFeatureExtractor cssExtractor) {
        this.hogFeatureExtractor = hogFeatureExtractor;
        this.glrlFeatureExtractor = glrlFeatureExtractor;
        this.momentsExtractor = momentsExtractor;
        this.haralickExtractor = haralickExtractor;
        this.gaborExtractor = gaborExtractor;
        this.cssExtractor = cssExtractor;
    }

    public HOGFeatureExtractor getHogFeatureExtractor() {
        return hogFeatureExtractor;
    }

    public GLRLFeatureExtractor getGlrlFeatureExtractor() {
        return glrlFeatureExtractor;
    }

    public MomentsExtractor getMomentsExtractor() {
        return momentsExtractor;
    }

    public HaralickFeatureExtractor getHaralickExtractor() {
        return haralickExtractor;
    }

    public GaborFeatureExtractor getGaborExtractor() {
        return gaborExtractor;
    }

    public CSSFeatureExtractor getCssExtractor() {
        return cssExtractor;
    }
}
