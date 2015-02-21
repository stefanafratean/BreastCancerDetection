package model.functions;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import util.Node;

public class FunctionHelper {
	private static Function plus = new Plus();
	private static Function minus = new Minus();
	private static Function multiplier = new Multiplier();
	private static Function safeDivision = new SafeDivision();
	private static Function sinus = new Sinus();
	private static Function cosinus = new Cosinus();
	private static Function ifOperator = new IfOperator();
	private static final List<Function> functions = Arrays.asList(plus, minus,
			multiplier, safeDivision);

	private FunctionHelper() {
		// class shoudn't be initialized
	}

	public static boolean nodeIsFunction(Node<Integer> node) {
		return node.getData() < 0;
	}

	/*
	 * returns a random value from the interval [-noOfFunctions; -1]
	 */
	public static int generateFunction(Random r) {
		int index = r.nextInt(functions.size()) + 1;
		index *= -1;
		return index;
	}

	/*
	 * return the function mapped with index
	 */
	public static Function getFunction(int index) {
		int realIndex = -1 * index - 1;
		return functions.get(realIndex);
	}

}
