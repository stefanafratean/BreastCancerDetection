package repository.extractors;

public interface FeatureExtractor {
	double[] extractDescriptors(String imageLocation);
}
