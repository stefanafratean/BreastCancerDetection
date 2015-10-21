package model;

import java.util.ArrayList;
import java.util.List;

public class Radiography {
    private final boolean withCancer;
    private List<Double> features;
    private String name;

    public Radiography(boolean withCancer, String name) {
        features = new ArrayList<Double>();
        this.withCancer = withCancer;
        this.name = name;
    }

    public void addDescriptors(double[] descriptorsList) {
        for (double aDescriptorsList : descriptorsList) {
            features.add(aDescriptorsList);
        }
    }

    public boolean isWithCancer() {
        return withCancer;
    }

    public List<Double> getFeatures() {
        return features;
    }

    public String getName() {
        return name;
    }
}
