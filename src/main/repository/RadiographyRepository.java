package repository;

import model.Radiography;
import repository.extractors.ExtractorsAggregator;
import util.File;

import java.util.ArrayList;
import java.util.List;

public class RadiographyRepository {
    public static final double TRAIN_PERCENTAGE = 67d / 100d;
    private List<ArrayList<Radiography>> subsetsList;

    private static int currentSubset;
    private RadiographyLoader radLoader;

    public RadiographyRepository(ExtractorsAggregator extractors, File file) {
        this.radLoader = new RadiographyLoader();
        List<Radiography> radiographyList = radLoader.loadRadiographies(extractors, file.getFile());

        subsetsList = initializeSubsetsList(radiographyList);
    }

    private List<ArrayList<Radiography>> initializeSubsetsList(List<Radiography> radiographyList) {
        int subsetsNo = 10;

        int subsetSize = radiographyList.size() / subsetsNo;
        List<ArrayList<Radiography>> subsetsList = new ArrayList<ArrayList<Radiography>>();
        ArrayList<Radiography> subsetList = new ArrayList<Radiography>();
        subsetList.add(radiographyList.get(0));
        for (int i = 1; i < radiographyList.size(); i++) {
            if (i % subsetSize == 0) {
                subsetsList.add(getClone(subsetList));
                subsetList.clear();
            }
            subsetList.add(radiographyList.get(i));
        }
        subsetsList.add(getClone(subsetList));
        return subsetsList;
    }

    private ArrayList<Radiography> getClone(ArrayList<Radiography> subsetList) {
        ArrayList<Radiography> clone = new ArrayList<Radiography>();
        for (Radiography rad : subsetList) {
            clone.add(rad);
        }
        return clone;
    }

    public List<Radiography> getTrainRadiographies() {
        List<Radiography> trainAndValidRads = getTrainAndValidationRads();
        int count = (int) Math.round(TRAIN_PERCENTAGE * (double) trainAndValidRads
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
        int count = (int) Math.round(TRAIN_PERCENTAGE * (double) trainAndValidRads
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

    public int getCancerRadNb() {
        return radLoader.getCancerRadNo();
    }

    public int getRadiographyNb() {
        return radLoader.getCancerRadNo() + radLoader.getNormalRadNo();
    }
}
