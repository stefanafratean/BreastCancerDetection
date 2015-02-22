package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AucValuesWrapper {
	private final List<Double> outputsForNormalRadios;
	private final List<Double> outputsForCancerRadios;
	private List<Double> tpRates;
	private List<Double> fpRates;

	public AucValuesWrapper() {
		outputsForNormalRadios = new ArrayList<Double>();
		outputsForCancerRadios = new ArrayList<Double>();
		tpRates = new ArrayList<Double>();
		fpRates = new ArrayList<Double>();
	}

	public void addFp(double fpRate) {
		fpRates.add(fpRate);
	}

	public void addTp(double tpRate) {
		tpRates.add(tpRate);
	}

	public Double getFp(int i) {
		return fpRates.get(i);
	}

	public Double getTp(int i) {
		return tpRates.get(i);
	}

	public void addCancerOutput(double value) {
		getOutputsForCancerRadios().add(value);
	}

	public void addNormalOutput(double value) {
		getOutputsForNormalRadios().add(value);
	}

	public void sortOutputCollections() {
		Collections.sort(getOutputsForCancerRadios());
		Collections.sort(getOutputsForNormalRadios());
	}

	/**
	 * @return the outputsForNormalRadios
	 */
	public List<Double> getOutputsForNormalRadios() {
		return outputsForNormalRadios;
	}

	/**
	 * @return the outputsForCancerRadios
	 */
	public List<Double> getOutputsForCancerRadios() {
		return outputsForCancerRadios;
	}

	public void sortRates() {
		Collections.sort(fpRates);
		Collections.sort(tpRates);
	}

	public double getFirstCancerOutput() {
		return outputsForCancerRadios.get(0);
	}

	public double getLastCancerOutput() {
		return outputsForCancerRadios.get(outputsForCancerRadios.size() - 1);
	}
}
