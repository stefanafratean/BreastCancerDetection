package model;

import java.util.ArrayList;
import java.util.List;

import model.terminals.Feature;
import model.terminals.HOGFeature;

public class Radiography {
	private final boolean withCancer;
	// private final HOGFeature hogFeature;
	private List<Double> descriptors;
	private String name;

	public Radiography(boolean withCancer) {
		descriptors = new ArrayList<Double>();
		// descriptors.add(hogFeature);
		this.withCancer = withCancer;
	}

	public void addDescriptors(double[] descriptorsList) {
		for (int i = 0; i < descriptorsList.length; i++) {
			descriptors.add(descriptorsList[i]);
		}
	}

	public boolean isWithCancer() {
		return withCancer;
	}

	public List<Double> getDescriptors() {
		return descriptors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
