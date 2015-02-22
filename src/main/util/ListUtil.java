package util;

import java.util.ArrayList;
import java.util.List;

class ListUtil {

	private ListUtil() {
		// prevent initialization
	}

	public static List<Double> convertToList(double[] list) {
		List<Double> converted = new ArrayList<Double>(list.length);
        for (double aList : list) {
            converted.add(aList);
        }
		return converted;
	}

}
