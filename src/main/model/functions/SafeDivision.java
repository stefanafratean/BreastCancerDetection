package model.functions;

public class SafeDivision implements TwoArgumentsFunction {

	/*
	 * If operator2 is 0 returns 0, else returns operator1/operator2
	 * (non-Javadoc)
	 * 
	 * @see model.functions.Function#compute(double, double)
	 */
	@Override
	public double compute(double operator1, double operator2) {
		if (operator2 == 0) {
			return 1;
		}
		return operator1 / operator2;
	}

}
