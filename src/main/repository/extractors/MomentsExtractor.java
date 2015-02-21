package repository.extractors;

public class MomentsExtractor implements FeatureExtractor{
//	static {
//		System.loadLibrary("libMomentsExtractor");
//	}
//
//	public native double[] extractMoments(String imageLocation);

	@Override
	public double[] extractDescriptors(String imageLocation) {
//		return extractMoments(imageLocation);
		return new double[6];
	}

}
