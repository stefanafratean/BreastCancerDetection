package repository.extractors;

public class CSSFeatureExtractor implements FeatureExtractor {
//	private static int i = 1;
//	static {
//		System.loadLibrary("libCSSExtractor");
//
//	}

//	public native double[] extractCSS(String imageLocation);

	@Override
	public double[] extractDescriptors(String imageLocation) {
//		try {
//			System.out.println(i++);
//			return extractCSS(imageLocation);
//		} catch (Exception e) {
//			System.out.println(imageLocation);
//		}
		return new double[0];
	}
}
