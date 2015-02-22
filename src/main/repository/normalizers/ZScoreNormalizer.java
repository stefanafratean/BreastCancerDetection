package repository.normalizers;

import model.Radiography;

import java.util.ArrayList;
import java.util.List;

public class ZScoreNormalizer implements RadiographyValuesNormalizer {
    private List<Double> means;
    private List<Double> standardDeviations;

    public ZScoreNormalizer() {
        means = new ArrayList<Double>();
        standardDeviations = new ArrayList<Double>();
    }

    @Override
    public void normalize(List<Radiography> radiographyList) {
        initializeLists(radiographyList.size());
        computeMeans(radiographyList);
        computeStdDevs(radiographyList);
        normalizeFeatures(radiographyList);
    }

    private void initializeLists(int size) {
        for (int i = 0; i < size; i++) {
            means.add(0d);
            standardDeviations.add(0d);
        }
    }

    private void normalizeFeatures(List<Radiography> radiographyList) {
        for (Radiography rad : radiographyList) {
            normalizeRadiography(rad);
        }
    }

    private void normalizeRadiography(Radiography radiography) {
        List<Double> features = radiography.getFeatures();
        for (int i = 0; i < features.size(); i++) {
            features.set(i, getNormalizedValue(i, features.get(i)));
        }
    }

    private Double getNormalizedValue(int position, Double value) {
        return (value - means.get(position)) / standardDeviations.get(position);
    }

    private void computeStdDevs(List<Radiography> radiographyList) {
        computeVariances(radiographyList);
        computeSquareRoots();
    }

    private void computeSquareRoots() {
        for (int i = 0; i < standardDeviations.size(); i++) {
            standardDeviations.set(i, Math.sqrt(standardDeviations.get(i)));
        }
    }

    private void computeVariances(List<Radiography> radiographyList) {
        for (Radiography rad : radiographyList) {
            List<Double> features = rad.getFeatures();
            for (int i = 0; i < features.size(); i++) {
                Double partialStdDev = standardDeviations.get(i) + Math.pow(features.get(i) - means.get(i), 2);
                standardDeviations.set(i, partialStdDev);
            }
        }

        divideBySize(standardDeviations, radiographyList);
    }

    private void computeMeans(List<Radiography> radiographyList) {
        for (Radiography rad : radiographyList) {
            List<Double> features = rad.getFeatures();
            for (int i = 0; i < features.size(); i++) {
                means.set(i, means.get(i) + features.get(i));
            }
        }

        divideBySize(means, radiographyList);
    }

    private void divideBySize(List<Double> list, List<Radiography> radiographyList) {
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i) / (double) radiographyList.size());
        }
    }
}
