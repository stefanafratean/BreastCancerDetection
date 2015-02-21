package results;

public class ResultsProcessor {
	private AccuracyComputer accComputer;
	private PrecisionComputer precComputer;
	private RappelComputer rapComputer;
	private double minAcc = Double.MAX_VALUE;
	private double maxAcc = 0;

	private double minRap = Double.MAX_VALUE;
	private double maxRap = 0;

	private double minPrec = Double.MAX_VALUE;
	private double maxPrec = 0;

	private WrongEntry globalWrong;
	private AccuracyComputer globalAccComputer;
	private PrecisionComputer globalPrecComputer;
	private RappelComputer globalRapComputer;

	public ResultsProcessor(int radiographyNb, int cancerNb, int iterationsNb) {
		this.accComputer = new AccuracyComputer(radiographyNb);
		this.precComputer = new PrecisionComputer(cancerNb);
		this.rapComputer = new RappelComputer(cancerNb);

		globalAccComputer = new AccuracyComputer(radiographyNb * iterationsNb);
		globalPrecComputer = new PrecisionComputer(cancerNb * iterationsNb);
		globalRapComputer = new RappelComputer(cancerNb * iterationsNb);

		globalWrong = new WrongEntry(0, 0);
	}

	public void computeAndShowResults(WrongEntry wrongEntry) {
		globalWrong.add(wrongEntry);
		double crossAcc = accComputer.computeAccuracy(wrongEntry);
		if (minAcc > crossAcc) {
			minAcc = crossAcc;
		}
		if (maxAcc < crossAcc) {
			maxAcc = crossAcc;
		}

		double crossPrec = precComputer.computePrecision(wrongEntry);
		if (minPrec > crossPrec) {
			minPrec = crossPrec;
		}
		if (maxPrec < crossPrec) {
			maxPrec = crossPrec;
		}

		double crossRap = rapComputer.computeRapel(wrongEntry);
		if (minRap > crossRap) {
			minRap = crossRap;
		}
		if (maxRap < crossRap) {
			maxRap = crossRap;
		}

		System.out.println("Cross acc: " + crossAcc);
		System.out.println("Cross prec: " + crossPrec);
		System.out.println("Cross rap: " + crossRap);

	}

	public void computeAndShowGlobals() {
		printGlobalWrong();
		double meanAcc = globalAccComputer.computeAccuracy(globalWrong);
		double meanPrec = globalPrecComputer.computePrecision(globalWrong);
		double meanRap = globalRapComputer.computeRapel(globalWrong);

		System.out.println();
		System.out.println("Max cross acc: " + maxAcc);
		System.out.println("Mean acc: " + meanAcc);
		System.out.println("Min cross acc: " + minAcc);
		System.out.println();

		System.out.println("Max cross prec: " + maxPrec);
		System.out.println("Mean prec: " + meanPrec);
		System.out.println("Min cross prec: " + minPrec);
		System.out.println();

		System.out.println("Max cross rap: " + maxRap);
		System.out.println("Mean rap: " + meanRap);
		System.out.println("Min cross rap: " + minRap);
	}

	private void printGlobalWrong() {
		System.out.println("Total: " + globalWrong.getTotalWrong());
		System.out.println("Total wrong cancer: "
				+ globalWrong.getWrongCancer());
		System.out.println("Total wrong normal: "
				+ globalWrong.getWrongNormal());
	}
}
