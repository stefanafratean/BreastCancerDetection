package learning;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import repository.extractors.MomentsExtractor;
import model.Radiography;
import model.terminals.Feature;
import model.terminals.GLRLFeature;
import model.terminals.HOGFeature;
import model.terminals.MomentsFeature;

public class TerminalHelper {
	private static List<Integer> constants = Arrays.asList(1, 2, 3, 4, 5, 6, 7,
			8, 9, -1, -2, -3, -4, -5, -6, -7, -8, -9);
	public final static int NB_OF_TERMINALS = 0
	// constants.size()
//	 + HOGFeature.ELEMENTS_NUMBER;
//	 + GLRLFeature.ELEMENTS_NUMBER
	 + 6; // momente
//	 + 14; // haralick
//	+ 60; // gabor
	// + 7781; //css

	private TerminalHelper() {
		// class shoudn't be initialized
	}

	/*
	 * returns a random value from the interval [0; noOfTerminals)
	 */
	public static int generateTerminal(Random r) {
		return r.nextInt(NB_OF_TERMINALS);
	}

	public static boolean shouldAddTerminal(Random r, boolean isFull) {
		if (isFull) {
			return false;
		}
		return r.nextDouble() < 0.5d;
	}

	/**
	 * Return the terminal value corresponding to index
	 * 
	 * @param radiography
	 *            the radiography that contains terminal values for each image
	 *            descriptor
	 * @param index
	 *            the index for which we retrieve the value
	 * @return
	 */
	public static double getMappedValue(Radiography radiography, Integer index) {
		int cumulatedSize = 0;

		List<Double> descriptors = radiography.getDescriptors();
		if (index < descriptors.size()) {
			return descriptors.get(index - cumulatedSize);
		}
		cumulatedSize += descriptors.size();

		double d = 0;
		try {
			d = constants.get(index - cumulatedSize);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println("Index: " + index);
			System.out.println("Desc length " + descriptors.size());
		}
		return d;
	}

}
