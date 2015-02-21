package svm;

import java.io.File;
import java.io.IOException;
import java.util.List;

import repository.RadiographyRepository;
import repository.extractors.HaralickFeatureExtractor;
import learning.TerminalHelper;
import model.Radiography;
import de.bwaldvogel.liblinear.Feature;
import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import de.bwaldvogel.liblinear.Parameter;
import de.bwaldvogel.liblinear.Problem;
import de.bwaldvogel.liblinear.SolverType;

public class SvmRunner {
	private List<Radiography> trainRadList;
	private List<Radiography> testRadList;

	private void loadRads() {
		HaralickFeatureExtractor haralickExtractor = new HaralickFeatureExtractor();
		RadiographyRepository trainRepo = new RadiographyRepository(null, null,
				null, haralickExtractor, null, null, "ddsm//DDSMTrain.txt");
		RadiographyRepository testRepo = new RadiographyRepository(null, null,
				null, haralickExtractor, null, null, "ddsm//DDSMTest.txt");

		trainRadList = trainRepo.getTrainRadiographies();
		testRadList = testRepo.getTestRadiographies();
	}

	public void test() throws IOException {
		loadRads();

		// http://liblinear.bwaldvogel.de/
		Problem problem = createProblem();
		
		SolverType solver = SolverType.L2R_LR;
		double C = 1.0;    // cost of constraints violation
		double eps = 0.01; // stopping criteria
		
		Parameter parameter = new Parameter(solver, C, eps);

		
		for (Feature[] nodes : problem.x) {
            for (Feature n : nodes) {
                System.out.print(n.getIndex() + " ");
            }
            System.out.println();
        }
		
		
		
		
		Model model = Linear.train(problem, parameter);
		File modelFile = new File("model");
		model.save(modelFile);
		// load model or use it directly
		model = Model.load(modelFile);
		
		int featureDescLength = testRadList.get(0).getDescriptors().size();
		Feature[] testFeature = new Feature[featureDescLength];
		for (int i = 0; i < featureDescLength; i++){
			double outputValue = testRadList.get(0).getDescriptors().get(i);
			testFeature[i] = new FeatureNode(i + 1, outputValue);
		}
		
		double res = Linear.predict(model, testFeature );
		System.out.println(res);
	}

	private Problem createProblem() {
		Problem problem = new Problem();
		int featureListSize = trainRadList.size();
		problem.l = featureListSize; // no of training examples
		problem.n = TerminalHelper.NB_OF_TERMINALS; // no of features
		problem.x = new Feature[featureListSize][trainRadList.get(0).getDescriptors().size()];
		problem.y = new double[featureListSize];
		
		for (int i = 0; i < featureListSize; i++) {
			Radiography rad = trainRadList.get(i);

			// feature nodes
			for (int j = 0; j < rad.getDescriptors().size(); j++) {
				Feature feature = new FeatureNode(j + 1, rad.getDescriptors().get(j));
				problem.x[i][j] = feature;
			}

			// target values
			if (rad.isWithCancer()) {
				problem.y[i] = 1;
			} else {
				problem.y[i] = 0;
			}
		}
		return problem;
	}
}
