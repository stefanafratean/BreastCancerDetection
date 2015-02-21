package model.terminals;

import java.util.List;

import util.ListUtil;

public class HOGFeature implements Feature {
	public final static int ELEMENTS_NUMBER = 3780;
	private final List<Double> descriptors;

	public HOGFeature(double[] descriptors) {
		this.descriptors = ListUtil.convertToList(descriptors);
	}

	public HOGFeature(List<Double> descriptors) {
		this.descriptors = descriptors;
	}

	@Override
	public List<Double> getDescriptors() {
		return descriptors;
	}

	@Override
	public String toString() {
		return "HOG descriptors: " + descriptors;
	}
}
