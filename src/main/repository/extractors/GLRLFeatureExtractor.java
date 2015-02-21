package repository.extractors;

public class GLRLFeatureExtractor implements FeatureExtractor {
//	static {
//		System.loadLibrary("libGLRLExtractor");
//	}
//
//	public native double[] extractGLRL(String imageLocation);

	@Override
	public double[] extractDescriptors(String imageLocation) {
 
//		 return extractGLRL(imageLocation);
		return new double[0];
	}
}
