package repository;

import model.Radiography;
import repository.extractors.ExtractorsAggregator;
import repository.extractors.FeatureExtractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadiographyLoader {
    private static final int MAX_RADIOGRAPHY_NUMBER = Integer.MAX_VALUE;

    private int cancerRadNo = 0;
    private int normalRadNo = 0;

    public List<Radiography> loadRadiographies(ExtractorsAggregator extractors,
                                               String fileName) {
        List<Radiography> radiographyList = getRadiographiesFromFile(extractors, fileName);
        System.out.println("Finished loading radiographies");
        return radiographyList;
    }

    public int getCancerRadNo() {
        return cancerRadNo;
    }

    public int getNormalRadNo() {
        return normalRadNo;
    }

    private List<Radiography> getRadiographiesFromFile(ExtractorsAggregator extractors, String fileName) {
        List<Radiography> radiographyList = new ArrayList<Radiography>();
        BufferedReader br;
        try {
            // "Radiographies.txt"
            br = new BufferedReader(new FileReader(fileName));
            String line;
            int c = 0;
            while (c < MAX_RADIOGRAPHY_NUMBER && (line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                try {
                    Radiography radiography;
                    if (tokens[1].trim().equals("n")) {
                        radiography = new Radiography(false);
                        normalRadNo++;
                    } else {
                        radiography = new Radiography(true);
                        cancerRadNo++;
                    }
                    radiographyList.add(radiography);
                    System.gc();
                } catch (Exception ex) {
                    System.out.println("Error" + tokens[0]);
                }
                c++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (FeatureExtractor extractor : extractors.getAllExtractors()) {
            loadFeaturesIfExtractorAvailable(radiographyList, extractor);
        }

        return radiographyList;
    }

    private void loadFeaturesIfExtractorAvailable(List<Radiography> radiographyList, FeatureExtractor extractor) {
        if (extractor != null) {
            loadFeaturesDirectly(extractor.getFileToExtractFrom(), radiographyList);
        }
    }

    private void loadFeaturesDirectly(String fileName, List<Radiography> radiographyList) {
        BufferedReader br;
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
                try {
                    radiographyList.get(c).addDescriptors(descriptorsList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                c++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
