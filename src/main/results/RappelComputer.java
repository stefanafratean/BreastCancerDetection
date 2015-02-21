package results;

public class RappelComputer {
	private int actualCancerNo;

	public RappelComputer(int cancerNo) {
		this.actualCancerNo = cancerNo;
	}

	public double computeRapel(WrongEntry wrongEntry) {
		// rap = (actualCancerNo - wrongNormal) / actualCancerNo
		return (double) (actualCancerNo - wrongEntry.getWrongNormal())
				/ (double) actualCancerNo;
	}
}
