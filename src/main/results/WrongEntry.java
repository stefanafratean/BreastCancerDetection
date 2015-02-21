package results;

public class WrongEntry {
	private int wrongCancer;
	private int wrongNormal;

	public WrongEntry(int wrongCancer, int wrongNormal) {
		this.wrongCancer = wrongCancer;
		this.wrongNormal = wrongNormal;
	}

	public int getTotalWrong() {
		return wrongCancer + wrongNormal;
	}

	public int getWrongCancer() {
		return wrongCancer;
	}

	public void setWrongCancer(int wrongCancer) {
		this.wrongCancer = wrongCancer;
	}

	public int getWrongNormal() {
		return wrongNormal;
	}

	public void setWrongNormal(int wrongNormal) {
		this.wrongNormal = wrongNormal;
	}

	public void add(WrongEntry wrongPerSubFold) {
		this.wrongCancer += wrongPerSubFold.getWrongCancer();
		this.wrongNormal += wrongPerSubFold.getWrongNormal();
	}
}
