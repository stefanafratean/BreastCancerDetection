package svm;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		SvmRunner runner = new SvmRunner();
		try {
			runner.test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
