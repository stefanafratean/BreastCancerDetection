package results;

public class AccuracyComputer {
	private int totalNo;

	public AccuracyComputer(int totalNo) {
		this.totalNo = totalNo;
	}

	public double computeAccuracy(WrongEntry wrongEntry) {
		// acc = (nrTotal - totalGresite) / nrTotal
		return (double) (totalNo - wrongEntry.getTotalWrong())
				/ (double) totalNo;
	}

}
