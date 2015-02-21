package repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Radiography;
import repository.extractors.CSSFeatureExtractor;
import repository.extractors.FeatureExtractor;
import repository.extractors.GLRLFeatureExtractor;
import repository.extractors.GaborFeatureExtractor;
import repository.extractors.HOGFeatureExtractor;
import repository.extractors.HaralickFeatureExtractor;
import repository.extractors.MomentsExtractor;
import util.Files;

public class RadiographyRepository {
	private static final int MAX_RADIOGRAPHY_NUMBER = Integer.MAX_VALUE;
	private final List<Radiography> radiographyList;
	private List<ArrayList<Radiography>> subsetsList;
	private List<FeatureExtractor> featureExtractors;
	boolean extractHaralick = false;
	boolean extractGabor = false;
	boolean extractCSS = false;
	boolean extractMoments = false;
	boolean extractGLRL = false;
	boolean extractHOG = false;
	private Files files;
	private static int currentSubset;
	private int subsetsNo = 10;
	private int cancerRadNo = 0;
	private int normalRadNo = 0;

	public RadiographyRepository(HOGFeatureExtractor hogFeatureExtractor,
			GLRLFeatureExtractor glrlFeatureExtractor,
			MomentsExtractor momentsExtractor,
			HaralickFeatureExtractor haralickExtractor,
			GaborFeatureExtractor gaborExtractor,
			CSSFeatureExtractor cssExtractor, String fileName) {

		files = new Files();
		featureExtractors = new ArrayList<FeatureExtractor>();
		setStateForExtractors(hogFeatureExtractor, glrlFeatureExtractor,
				momentsExtractor, haralickExtractor, gaborExtractor,
				cssExtractor);
		radiographyList = new ArrayList<Radiography>();
		getRadiographiesFromFile(fileName);
		initializeSubsetsList();
		radiographyList.clear();
		System.out.println("Finished loading radiographies");
	}

	private void initializeSubsetsList() {
		int subsetSize = radiographyList.size() / subsetsNo;
		subsetsList = new ArrayList<ArrayList<Radiography>>();
		ArrayList<Radiography> subsetList = new ArrayList<Radiography>();
		subsetList.add(radiographyList.get(0));
		for (int i = 1; i < radiographyList.size(); i++) {
			if (i % subsetSize == 0) {
				this.subsetsList.add(getClone(subsetList));
				subsetList.clear();
			}
			subsetList.add(radiographyList.get(i));
		}
		subsetsList.add(getClone(subsetList));
	}

	private ArrayList<Radiography> getClone(ArrayList<Radiography> subsetList) {
		ArrayList<Radiography> clone = new ArrayList<Radiography>();
		for (Radiography rad : subsetList) {
			clone.add(rad);
		}
		return clone;
	}

	private void setStateForExtractors(HOGFeatureExtractor hogFeatureExtractor,
			GLRLFeatureExtractor glrlFeatureExtractor,
			MomentsExtractor momentsExtractor,
			HaralickFeatureExtractor haralickExtractor,
			GaborFeatureExtractor gaborExtractor,
			CSSFeatureExtractor cssExtractor) {
		if (hogFeatureExtractor != null) {
			extractHOG = true;
		}
		if (haralickExtractor != null) {
			extractHaralick = true;
		}
		if (gaborExtractor != null) {
			extractGabor = true;
		}
		if (cssExtractor != null) {
			extractCSS = true;
		}
		if (momentsExtractor != null) {
			extractMoments = true;
		}
		if (glrlFeatureExtractor != null) {
			extractGLRL = true;
		}
	}

	// if this extractor was not selected by the user no descriptors are
	// computed
	private double[] getDescriptorsFromImage(FeatureExtractor extractor,
			String imageLocation) {
		if (extractor != null) {
			return extractor.extractDescriptors(imageLocation);
		}
		return new double[0];
	}

	private void getRadiographiesFromFile(String fileName) {
		BufferedReader br = null;
		try {
			// "Radiographies.txt"
			br = new BufferedReader(new FileReader(fileName));
			String line;
			int c = 0;
			while (c < MAX_RADIOGRAPHY_NUMBER && (line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				try {
					Radiography radiography = null;
					if (tokens[1].trim().equals("n")) {
						radiography = new Radiography(false);
						normalRadNo++;
					} else {
						radiography = new Radiography(true);
						cancerRadNo++;
					}
					radiography.setName(tokens[0]);
					addDescriptorsToRadiography(tokens, radiography);
					radiographyList.add(radiography);
					System.gc();
				} catch (Exception ex) {
					System.out.println("Eroare" + tokens[0]);
				}
				c++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (extractHOG) {
			String hogFileName = files.getHOGFile();
			loadFeaturesDirectly(hogFileName);
		}
		if (extractHaralick) {
			String haralickFileName = files.getHaralickFile();
			loadFeaturesDirectly(haralickFileName);
		}
		if (extractGabor) {
			String gaborFileName = files.getGaborFile();
			loadFeaturesDirectly(gaborFileName);
		}
		if (extractCSS) {
			String cssFileName = files.getCSSFile();
			loadFeaturesDirectly(cssFileName);
		}
		if (extractMoments) {
			String momentsFileName = files.getMomentsFile();
			loadFeaturesDirectly(momentsFileName);
		}
		if (extractGLRL) {
			String momentsFileName = files.getGLRLFile();
			loadFeaturesDirectly(momentsFileName);
		}
	}

	private void loadFeaturesDirectly(String fileName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			String line;
			int c = 0;
			while (c < MAX_RADIOGRAPHY_NUMBER && (line = br.readLine()) != null) {
				String[] tokens = line.trim().split(" ");
				double[] descriptorsList = new double[tokens.length];
				for (int i = 0; i < tokens.length; i++) {
					descriptorsList[i] = Double.parseDouble(tokens[i]);
				}
				if (descriptorsList.length != 7781) {
					System.out.println(descriptorsList.length);
				}
				try {
					radiographyList.get(c).addDescriptors(descriptorsList);
				} catch (Exception e) {
					@SuppressWarnings("unused")
					int a = c;
				}
				c++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addDescriptorsToRadiography(String[] imageLocation,
			Radiography radiography) {
		for (FeatureExtractor extractor : featureExtractors) {
			double[] descriptors = getDescriptorsFromImage(extractor,
					imageLocation[0]);
			radiography.addDescriptors(descriptors);
		}
	}

	// public List<Radiography> getRadiographies() {
	// return radiographyList;
	// }

	public List<Radiography> getTrainRadiographies() {
		List<Radiography> trainAndValidRads = getTrainAndValidationRads();
		int count = (int) Math.round(67d / 100d * (double) trainAndValidRads
				.size());
		List<Radiography> validationRadiographies = new ArrayList<Radiography>();
		for (int i = 0; i < count; i++) {
			validationRadiographies.add(trainAndValidRads.get(i));
		}

		return validationRadiographies;
	}

	private List<Radiography> getTrainAndValidationRads() {
		List<Radiography> trainList = new ArrayList<Radiography>();
		for (int i = 0; i < subsetsList.size(); i++) {
			if (i != currentSubset) {
				trainList.addAll(subsetsList.get(i));
			}
		}
		return trainList;
	}

	public List<Radiography> getValidationRadiographies() {
		List<Radiography> trainAndValidRads = getTrainAndValidationRads();
		int count = (int) Math.round(67d / 100d * (double) trainAndValidRads
				.size());
		List<Radiography> validationRadiographies = new ArrayList<Radiography>();
		for (int i = count; i < trainAndValidRads.size(); i++) {
			validationRadiographies.add(trainAndValidRads.get(i));
		}

		return validationRadiographies;
	}

	public List<Radiography> getTestRadiographies() {
		return subsetsList.get(currentSubset);
	}

	public void increaseCurrentSubset() {
		currentSubset++;
	}

	public void resetCurrentSubset() {
		currentSubset = 0;
	}

	public int getCancerRadNo() {
		return cancerRadNo;
	}

	public int getRadiographyNb() {
		return cancerRadNo + normalRadNo;
	}
}
