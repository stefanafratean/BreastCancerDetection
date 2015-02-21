package repository.extractors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class CSSWriter {
	private CSSFeatureExtractor extractor = new CSSFeatureExtractor();
	private List<double[]> features = new ArrayList<double[]>();
	
	public void write(){
		load("bcdr//BCDRTest.txt");
		write("bcdr//BCDRTestCSS.txt");
		System.out.println("Done");
		
		features = new ArrayList<double[]>();
		load("bcdr//BCDRTrain.txt");
		write("bcdr//BCDRTrainCSS.txt");
	}

	private void write(String fileName) {
		File file = new File(fileName);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for (double[] d : features){
				StringBuilder br = new StringBuilder();
				for (int i = 0; i < d.length; i++){
					br.append(d[i]).append(" ");
				}
				output.write(br.toString().trim() + "\r\n");
			}
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void load(String inputName) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputName));
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				features.add(extractor.extractDescriptors(tokens[0]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
