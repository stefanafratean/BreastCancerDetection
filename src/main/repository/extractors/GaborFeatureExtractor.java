package repository.extractors;

import ij.process.ColorProcessor;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import de.lmu.ifi.dbs.jfeaturelib.features.Gabor;

public class GaborFeatureExtractor implements FeatureExtractor{
	@Override
	public double[] extractDescriptors(String imageLocation) {
		List<double[]> features;
		try {
			features = extractFeatures(imageLocation);
			return features.get(0);
		} catch (IOException e) {
			System.out.println("Could not read image: " + imageLocation);
		}
		return new double[0];
	}

	private List<double[]> extractFeatures(String imageLocation) throws IOException {
		File f = new File(imageLocation);
		ColorProcessor image = new ColorProcessor(ImageIO.read(f).getScaledInstance(64,
				128, Image.SCALE_SMOOTH));


		Gabor descriptor = new Gabor();
		
		descriptor.run(image);
		return descriptor.getFeatures();
	}

}
