package results;

public class PrecisionComputer {
	private int actualCancerNo;

	public PrecisionComputer(int cancerNo) {
		this.actualCancerNo = cancerNo;
	}

	public double computePrecision(WrongEntry wrongEntry) {
		// prec = (actualCancerNo - wrongNormal) / (actualCancerNorC -
		// wrongNormal + wrongCancer)
		return (double) (actualCancerNo - wrongEntry.getWrongNormal())
				/ (double) (actualCancerNo - wrongEntry.getWrongNormal() + wrongEntry
						.getWrongCancer());
	}
}
