package repository;

import model.Radiography;
import repository.extractors.ExtractorsAggregator;
import repository.extractors.FeatureExtractor;
import util.Files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadiographyLoader {
    private static final int MAX_RADIOGRAPHY_NUMBER = Integer.MAX_VALUE;
    private List<FeatureExtractor> featureExtractors;

    boolean extractHaralick = false;
    boolean extractGabor = false;
    boolean extractCSS = false;
    boolean extractMoments = false;
    boolean extractGLRL = false;
    boolean extractHOG = false;

    private Files files;

    private int cancerRadNo = 0;
    private int normalRadNo = 0;

    public List<Radiography> loadRadiographies(ExtractorsAggregator extractors,
                                               String fileName) {
        files = new Files();
        featureExtractors = new ArrayList<FeatureExtractor>();
        setStateForExtractors(extractors);
        List<Radiography> radiographyList = getRadiographiesFromFile(fileName);
        System.out.println("Finished loading radiographies");
        return radiographyList;
    }

    public int getCancerRadNo() {
        return cancerRadNo;
    }

    public int getNormalRadNo() {
        return normalRadNo;
    }

    private void setStateForExtractors(ExtractorsAggregator extractors) {
        // TODO refactor me with strategy pattern
        if (extractors.getHogFeatureExtractor() != null) {
            extractHOG = true;
        }
        if (extractors.getHaralickExtractor() != null) {
            extractHaralick = true;
        }
        if (extractors.getGaborExtractor() != null) {
            extractGabor = true;
        }
        if (extractors.getCssExtractor() != null) {
            extractCSS = true;
        }
        if (extractors.getMomentsExtractor() != null) {
            extractMoments = true;
        }
        if (extractors.getGlrlFeatureExtractor() != null) {
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

    private List<Radiography> getRadiographiesFromFile(String fileName) {
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

        // TODO refactor me with strategy pattern
        if (extractHOG) {
            String hogFileName = files.getHOGFile();
            loadFeaturesDirectly(hogFileName, radiographyList);
        }
        if (extractHaralick) {
            String haralickFileName = files.getHaralickFile();
            loadFeaturesDirectly(haralickFileName, radiographyList);
        }
        if (extractGabor) {
            String gaborFileName = files.getGaborFile();
            loadFeaturesDirectly(gaborFileName, radiographyList);
        }
        if (extractCSS) {
            String cssFileName = files.getCSSFile();
            loadFeaturesDirectly(cssFileName, radiographyList);
        }
        if (extractMoments) {
            String momentsFileName = files.getMomentsFile();
            loadFeaturesDirectly(momentsFileName, radiographyList);
        }
        if (extractGLRL) {
            String momentsFileName = files.getGLRLFile();
            loadFeaturesDirectly(momentsFileName, radiographyList);
        }

        return radiographyList;
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
}
