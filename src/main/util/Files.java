package util;

public class Files {
	private String trainFile;
	private String testFile;
	private String file;

	public Files() {
		String MIASTrain = "mias/MIASTrain.txt";
		String MIASTest = "mias/MIASTest.txt";
		
		String mias = "mias/MIAS.txt";
		String ddsm = "ddsm/DDSM.txt";
		String ddsmShort = "ddsm_short/DDSMShort.txt";
		String bcdrFilm = "bcdr_film/BCDRFilm.txt";
		String bcdrDigital = "bcdr/BCDRDigital.txt";

		trainFile = MIASTrain;
		testFile = MIASTest;
		file = mias;
	}

	public String getTrainFile() {
		return trainFile;
	}

	public String getTestFile() {
		return testFile;
	}

	public String getMomentsFile() {
		return file.split("\\.")[0] + "Moments.txt";
	}
	
	public String getHaralickFile() {
		return file.split("\\.")[0] + "Haralick.txt";
	}
	
	public String getGLRLFile() {
		return file.split("\\.")[0] + "GLRL.txt";
	}

	
	public String getGaborFile() {
		return file.split("\\.")[0] + "Gabor.txt";
	}
	
	public String getHOGFile() {
		return file.split("\\.")[0] + "HOG.txt";
	}
	
	public String getCSSFile() {
		return file.split("\\.")[0] + "CSS.txt";
	}

	public String getFile() {
		return file;
	}
}
