package util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	private ListUtil() {
		// prevent initialization
	}

	public static List<Double> convertToList(double[] list) {
		List<Double> converted = new ArrayList<Double>(list.length);
		for (int i = 0; i < list.length; i++) {
			converted.add(list[i]);
		}
		return converted;
	}

}
