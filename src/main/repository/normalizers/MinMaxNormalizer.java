package repository.normalizers;

import model.Radiography;

import java.util.ArrayList;
import java.util.List;

/**
 * Scales data to fall within the range [0,1]
 */
public class MinMaxNormalizer implements RadiographyValuesNormalizer {
    private List<Double> minValues;
    private List<Double> maxValues;

    public MinMaxNormalizer() {
        minValues = new ArrayList<Double>();
        maxValues = new ArrayList<Double>();
    }

    @Override
    public void normalize(List<Radiography> radiographyList) {
        computeMinAndMaxValues(radiographyList);
        normalizeRadiographies(radiographyList);
    }

    private void computeMinAndMaxValues(List<Radiography> radiographyList) {
        initializeMinMax(radiographyList);
        for (Radiography rad : radiographyList) {
            List<Double> features = rad.getFeatures();
            for (int i = 0; i < features.size(); i++) {
                if (minValues.get(i) > features.get(i)) {
                    minValues.set(i, features.get(i));
                }
                if (maxValues.get(i) < features.get(i)) {
                    maxValues.set(i, features.get(i));
                }
            }
        }
    }


    private void normalizeRadiographies(List<Radiography> radiographyList) {
        for (Radiography rad : radiographyList) {
            normalizeFeatures(rad);
        }
    }

    private void normalizeFeatures(Radiography rad) {
        List<Double> features = rad.getFeatures();
        for (int i = 0; i < features.size(); i++) {
            features.set(i, getNormalizedValue(features.get(i), i));
        }
    }

    private double getNormalizedValue(Double value, int index) {
        return (value - minValues.get(index)) / (maxValues.get(index) - minValues.get(index));
    }

    private void initializeMinMax(List<Radiography> radiographyList) {
        Radiography firstRad = radiographyList.get(0);
        for (Double feature : firstRad.getFeatures()) {
            minValues.add(feature);
            maxValues.add(feature);
        }
    }
}
