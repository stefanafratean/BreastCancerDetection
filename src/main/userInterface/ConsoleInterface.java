package userInterface;

import learning.LearningStarter;
import repository.extractors.ExtractorsAggregator;
import repository.extractors.GLRLFeatureExtractor;
import repository.extractors.HaralickFeatureExtractor;
import repository.extractors.MomentsExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInterface {
    private LearningStarter learningStarter;

    public ConsoleInterface(LearningStarter learningStarter) {
        this.learningStarter = learningStarter;
    }

    public void run() {
        printMenuOptions();
        ExtractorsAggregator extractors = readExtractors();

        learningStarter.startLearning(extractors);
    }

    private ExtractorsAggregator readExtractors() {
        String descriptor = readString();
        if (descriptor.equals("1")) {
            return new ExtractorsAggregator.Builder().moments(new MomentsExtractor()).build();
        } else if (descriptor.equals("2")) {
            return new ExtractorsAggregator.Builder().glrl(new GLRLFeatureExtractor()).build();
        } else if (descriptor.equals("3")) {
            return new ExtractorsAggregator.Builder().haralick(new HaralickFeatureExtractor()).build();
        } else if (descriptor.equals("4")) {
            return new ExtractorsAggregator.Builder().moments(new MomentsExtractor()).glrl(new GLRLFeatureExtractor()).build();
        } else {
            System.out.println("Error: unknown desc type");
        }

        return null;
    }

    private void printMenuOptions() {
        System.out.println("Select descriptor:");
        System.out.println("1. Moments");
        System.out.println("2. GLRL");
        System.out.println("3. Haralick");
        System.out.println("4. Moments + GLRL");
    }

    private String readString() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
