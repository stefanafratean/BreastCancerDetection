package repository.extractors;

import java.util.Arrays;
import java.util.List;

public class ExtractorsAggregator {
    private final HOGFeatureExtractor hogFeatureExtractor;
    private final GLRLFeatureExtractor glrlFeatureExtractor;
    private final MomentsExtractor momentsExtractor;
    private final HaralickFeatureExtractor haralickExtractor;
    private final GaborFeatureExtractor gaborExtractor;
    private final CSSFeatureExtractor cssExtractor;

    public static class Builder {
        private HOGFeatureExtractor hogFeatureExtractor = null;
        private GLRLFeatureExtractor glrlFeatureExtractor = null;
        private MomentsExtractor momentsExtractor = null;
        private HaralickFeatureExtractor haralickExtractor = null;
        private GaborFeatureExtractor gaborExtractor = null;
        private CSSFeatureExtractor cssExtractor = null;

        public Builder hog(HOGFeatureExtractor hogFeatureExtractor) {
            this.hogFeatureExtractor = hogFeatureExtractor;
            return this;
        }

        public Builder glrl(GLRLFeatureExtractor glrlFeatureExtractor) {
            this.glrlFeatureExtractor = glrlFeatureExtractor;
            return this;
        }

        public Builder moments(MomentsExtractor momentsExtractor) {
            this.momentsExtractor = momentsExtractor;
            return this;
        }

        public Builder haralick(HaralickFeatureExtractor haralickExtractor) {
            this.haralickExtractor = haralickExtractor;
            return this;
        }

        public Builder gabor(GaborFeatureExtractor gaborExtractor) {
            this.gaborExtractor = gaborExtractor;
            return this;
        }

        public Builder css(CSSFeatureExtractor cssExtractor) {
            this.cssExtractor = cssExtractor;
            return this;
        }

        public ExtractorsAggregator build() {
            return new ExtractorsAggregator(this);
        }

    }

    private ExtractorsAggregator(Builder builder) {
        hogFeatureExtractor = builder.hogFeatureExtractor;
        momentsExtractor = builder.momentsExtractor;
        glrlFeatureExtractor = builder.glrlFeatureExtractor;
        haralickExtractor = builder.haralickExtractor;
        cssExtractor = builder.cssExtractor;
        gaborExtractor = builder.gaborExtractor;
    }

    public List<FeatureExtractor> getAllExtractors() {
        return Arrays.asList(cssExtractor, gaborExtractor, glrlFeatureExtractor, haralickExtractor, hogFeatureExtractor, momentsExtractor);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FeatureExtractor extractor : getAllExtractors()) {
            if (extractor != null) {
                sb.append(extractor.getClass().getSimpleName());
            }
        }
        return sb.toString();
    }

}
