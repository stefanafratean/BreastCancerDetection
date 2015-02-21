package model;

public class ConfusionValuesWrapper {
	private double truePositive = 0d;
	private double trueNegative = 0d;
	private double falsePositive = 0d;
	private double falseNegative = 0d;

	public void increaseTP() {
		truePositive++;
	}

	public void increaseTN() {
		trueNegative++;
	}

	public void increaseFP() {
		falsePositive++;
	}

	public void increaseFN() {
		falseNegative++;
	}

	public double getOverallAccuracy() {
		return (truePositive + trueNegative)
				/ (trueNegative + truePositive + falseNegative + falsePositive);
	}

}
