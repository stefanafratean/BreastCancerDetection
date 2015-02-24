package userInterface;

import learning.ChromosomeOperator;
import learning.LearningStarter;
import learning.TerminalOperator;
import repository.RadiographyRepository;
import repository.extractors.ExtractorsAggregator;
import repository.extractors.GLRLFeatureExtractor;
import repository.extractors.HaralickFeatureExtractor;
import repository.extractors.MomentsExtractor;
import util.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleInterface {

    public void run() {
        printFilesOptions();
        String fileName = readFileName();
        File file = new File(fileName);
        printDescriptorsOptions();
        ExtractorsAggregator extractors = readExtractors(file);

        RadiographyRepository radiographyRepository = new RadiographyRepository(extractors, file);
        ChromosomeOperator chromosomeOperator = new ChromosomeOperator(new TerminalOperator(extractors));
        LearningStarter learningStarter = new LearningStarter(radiographyRepository, chromosomeOperator);
        learningStarter.startLearning();
    }

    private void printFilesOptions() {
        System.out.println("Select descriptor:");
        System.out.println("1. MIAS");
        System.out.println("2. DDSM");
        System.out.println("3. BCDR Film");
        System.out.println("4. BCDR Digital");
        System.out.println("5. DDSM short version");
    }

    private String readFileName() {
        String nameCode = readString();
        if (nameCode.equals("1")) {
            return "mias";
        } else if (nameCode.equals("2")) {
            return "ddsm";
        } else if (nameCode.equals("3")) {
            return "bcdr_film";
        } else if (nameCode.equals("4")) {
            return "bcdr_dig";
        } else if (nameCode.equals("5")) {
            return "ddsm_short";
        } else {
            System.err.println("Error: unknown file type");
        }

        return null;
    }

    private ExtractorsAggregator readExtractors(File file) {
        String descriptor = readString();
        if (descriptor.equals("1")) {
            return new ExtractorsAggregator.Builder().moments(new MomentsExtractor(file)).build();
        } else if (descriptor.equals("2")) {
            return new ExtractorsAggregator.Builder().glrl(new GLRLFeatureExtractor(file)).build();
        } else if (descriptor.equals("3")) {
            return new ExtractorsAggregator.Builder().haralick(new HaralickFeatureExtractor(file)).build();
        } else if (descriptor.equals("4")) {
            return new ExtractorsAggregator.Builder().moments(new MomentsExtractor(file)).glrl(new GLRLFeatureExtractor(file)).build();
        } else {
            System.out.println("Error: unknown desc type");
        }

        return null;
    }

    private void printDescriptorsOptions() {
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
