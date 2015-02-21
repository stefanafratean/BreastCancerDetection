package model.functions;

public class Multiplier implements TwoArgumentsFunction {

	@Override
	public double compute(double operator1, double operator2) {
		return operator1 * operator2;
	}

}
