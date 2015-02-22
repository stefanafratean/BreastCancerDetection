package repository;


public class FitnessHelper {

	private FitnessHelper() {

	}

	public static boolean getCancerDecision(double chromosomeOutput) {
		return chromosomeOutput < 0;
	}

	public static boolean fitnessHasBiggerValue(double fitness1, double fitness2) {
        return fitness1 > fitness2;
    }

	public static boolean fitnessAreEqual(double fitness1, double fitness2) {
		return fitness1 == fitness2;
	}

}
