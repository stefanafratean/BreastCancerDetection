package repository.extractors;

//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.core.MatOfFloat;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.Size;
//import org.opencv.highgui.Highgui;
//import org.opencv.imgproc.Imgproc;
//import org.opencv.objdetect.HOGDescriptor;

public class HOGFeatureExtractor implements FeatureExtractor {
	// private static final Size WIN_SIZE = new Size(64, 128);
	// private static final Size CELL_SIZE = new Size(8, 8);
	// private static final int GRADIENT_BIN_SIZE = 9;
	// private static final int CELLS_IN_X_DIR;
	// private static final int CELLS_IN_Y_DIR;
	// private static final int BLOCKS_IN_X_DIR;
	// private static final int BLOCKS_IN_Y_DIR;
	// private static final int DESCRIPTORS_LENGTH = 3780;
	//
	// static {
	// CELLS_IN_X_DIR = (int) (WIN_SIZE.width / CELL_SIZE.width);
	// CELLS_IN_Y_DIR = (int) (WIN_SIZE.height / CELL_SIZE.height);
	// BLOCKS_IN_X_DIR = CELLS_IN_X_DIR - 1;
	// BLOCKS_IN_Y_DIR = CELLS_IN_Y_DIR - 1;
	// }
	//
	// static{
	// System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	// }

	@Override
	public double[] extractDescriptors(String imageLocation) {
		// HOGDescriptor hog = new HOGDescriptor();
		//
		// MatOfFloat descriptorValues = new MatOfFloat();
		// MatOfPoint locs = new MatOfPoint();
		// Mat image = getImage(imageLocation);
		// hog.compute(image, descriptorValues, new Size(8, 8), new Size(0, 0),
		// locs);
		//
		// return getDescriptorsAsArray(descriptorValues);
		return new double[0];
	}

	// private Mat getImage(String location) {
	// Mat img = Highgui.imread(location, Highgui.CV_LOAD_IMAGE_GRAYSCALE);
	//
	// Mat resizedImage = new Mat();
	// Size sz = new Size(64, 128);
	// Imgproc.resize(img, resizedImage, sz);
	// return resizedImage;
	// }
	//
	// private double[] getDescriptorsAsArray(MatOfFloat descriptorValues) {
	// double[] descriptorsArray = new double[DESCRIPTORS_LENGTH];
	// int descriptorDataIdx = 0;
	// for (int blockx = 0; blockx < BLOCKS_IN_X_DIR; blockx++) {
	// // for (all block x pos)
	// for (int blocky = 0; blocky < BLOCKS_IN_Y_DIR; blocky++) {
	// // for (all block y pos)
	// for (int cellNr = 0; cellNr < 4; cellNr++) {
	// // for (all cells)
	// for (int bin = 0; bin < GRADIENT_BIN_SIZE; bin++) {
	// // for (all bins)
	// descriptorsArray[descriptorDataIdx] = descriptorValues
	// .get(descriptorDataIdx, 0)[0];
	// descriptorDataIdx++;
	// }
	// }
	// }
	// }
	// return descriptorsArray;
	// }

}
