package model;

import java.util.ArrayList;
import java.util.List;

public class Radiography {
    private final boolean withCancer;
    private List<Double> features;

    public Radiography(boolean withCancer) {
        features = new ArrayList<Double>();
        this.withCancer = withCancer;
    }

    public void addDescriptors(double[] descriptorsList) {
        for (int i = 0; i < descriptorsList.length; i++) {
            features.add(descriptorsList[i]);
        }
    }

    public boolean isWithCancer() {
        return withCancer;
    }

    public List<Double> getFeatures() {
        return features;
    }
}
