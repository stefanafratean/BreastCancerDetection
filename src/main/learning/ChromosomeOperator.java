package learning;

import static learning.TerminalHelper.generateTerminal;
import static learning.TerminalHelper.shouldAddTerminal;
import static model.functions.FunctionHelper.generateFunction;

import java.util.Random;

import model.Chromosome;
import model.Radiography;
import model.functions.Function;
import model.functions.FunctionHelper;
import model.functions.OneArgumentFunction;
import model.functions.TwoArgumentsFunction;
import util.Node;
import util.Tree;

public class ChromosomeOperator {
	private static boolean switchFlag;
	public static final int MAX_CHROMOSOME_DEPTH = (int) (Math.round(Math
			.log(TerminalHelper.NB_OF_TERMINALS) / Math.log(2)));

	private ChromosomeOperator() {

	}

	public static Chromosome xo(Chromosome mother, Chromosome father, Random r) {

		Node<Integer> node = setFlagAndBuildChild(mother, father, r);
		Tree<Integer> tree = new Tree<Integer>(node);

		return new Chromosome(tree);
	}

	public static Chromosome mutation(Chromosome chromosome, Random r) {
		Node<Integer> node = getRandomNode(chromosome.getRepresentation()
				.getRoot(), r);
//		 int maxMutationDepth = MAX_CHROMOSOME_DEPTH - node.getDepth();
//		 Node<Integer> mutation = createChromosome(maxMutationDepth, r,
//		 r.nextBoolean()).getRootNode();
//		 node.setData(mutation.getData());
//		 node.setLeft(mutation.getLeft());
//		 node.setRight(mutation.getRight());
		if (FunctionHelper.nodeIsFunction(node)) {
			node.setData(getDifferentFunction(node.getData(), r));
		} else {
			node.setData(getDifferentTerminal(node.getData(), r));
		}
		return chromosome;
	}

	/**
	 * Creates and initializes a chromosome using the method full or using the
	 * method grow, depending on the value of the isFull parameter.
	 * 
	 * @param maxDepth
	 *            represents the maximum allowed depth of the chromosome
	 * @param r
	 *            random number used to choose terminals and functions
	 * @param isFull
	 *            should be set to true if we want the chromosome to be
	 *            initializes with the full method, false if we want it to be
	 *            initialized with the grow method.
	 * @return
	 */
	public static Chromosome createChromosome(int maxDepth, Random r,
			boolean isFull) {
		Chromosome chromosome = new Chromosome(maxDepth, r);
		initChromozome(maxDepth, 0, r, chromosome.getRootNode(), isFull);
		return chromosome;
	}

	private static void initChromozome(int maxDepth, int currentDepth,
			Random r, Node<Integer> currentNode, boolean isFull) {
		
		// add terminal
		if (currentDepth != 0 && (shouldAddTerminal(r, isFull) || currentDepth == maxDepth)) {
			currentNode.setData(generateTerminal(r));
			return;
		}
		// add function
		currentNode.setData(generateFunction(r));

		// initialize left
		Node<Integer> leftNode = new Node<Integer>();
		currentNode.setLeft(leftNode);
		leftNode.setParent(currentNode);
		initChromozome(maxDepth, currentDepth + 1, r, leftNode, isFull);

		// initialize right
		Node<Integer> rightNode = new Node<Integer>();
		currentNode.setRight(rightNode);
		rightNode.setParent(currentNode);
		initChromozome(maxDepth, currentDepth + 1, r, rightNode, isFull);
	}

	/*
	 * Returns a number that is the result of the discriminant function
	 * represented by the chromosome
	 */
	public static double getOuputValue(Chromosome chromosome,
			Radiography radiography) {
		return getOuputValue(chromosome.getRepresentation().getRoot(),
				radiography);
	}

	private static double getOuputValue(Node<Integer> node,
			Radiography radiography) {
		if (!FunctionHelper.nodeIsFunction(node)) {
			return TerminalHelper.getMappedValue(radiography, node.getData());
			// return hog.getDescriptors().get(node.getData());
		} else {
			Function f = FunctionHelper.getFunction(node.getData());

			double leftSideResult = getOuputValue(node.getLeft(), radiography);
			double rightSideResult = getOuputValue(node.getRight(), radiography);
			return ((TwoArgumentsFunction)f).compute(leftSideResult, rightSideResult);
		}
	}

	private static int getDifferentTerminal(int current, Random r) {
		int newTerminal;
		while (true) {
			newTerminal = TerminalHelper.generateTerminal(r);
			if (current != newTerminal) {
				return newTerminal;
			}
		}
	}

	private static int getDifferentFunction(int current, Random r) {
		int newTerminal;
		while (true) {
			newTerminal = FunctionHelper.generateFunction(r);
			if (current != newTerminal) {
				return newTerminal;
			}
		}
	}

	private static Node<Integer> setFlagAndBuildChild(Chromosome mother,
			Chromosome father, Random r) {
		switchFlag = true;
		Node<Integer> node = buildChild(mother.getRepresentation().getRoot(),
				father.getRepresentation().getRoot(), r, 0);
		switchFlag = true;
		return node;
	}

	private static Node<Integer> buildChild(Node<Integer> parentNode1,
			Node<Integer> parentNode2, Random r, int currentDepth) {
		if (parentNode1 != null) {
			Node<Integer> childNode = new Node<Integer>();
			childNode.setParent(parentNode1.getParent());
			if (maxAllowedDepthAndFunction(parentNode1, currentDepth)) {
				childNode.setData(TerminalHelper.generateTerminal(r));
				childNode.setLeft(null);
				childNode.setRight(null);
				return childNode;
			}
			childNode.setData(parentNode1.getData());
			// TODO see if can use lambda expr instead
			if (currentDepth == 0 || !switchParent(r)) {
			//	if (FunctionHelper.nodeIsFunction(childNode)){
				childNode.addLeft(buildChild(parentNode1.getLeft(),
						parentNode2, r, currentDepth + 1));
				childNode.addRight(buildChild(parentNode1.getRight(),
						parentNode2, r, currentDepth + 1));
			//	}
			} else {
				Node<Integer> nodeFromOtherParent = buildChild(getRandomNode(parentNode2, r),
						null, r, currentDepth + 1);
				childNode.setData(nodeFromOtherParent.getData());
				childNode.setLeft(nodeFromOtherParent.getLeft());
				childNode.setRight(nodeFromOtherParent.getRight());
			}
//			if (!switchParent(r)) {
//
//			} else {
//				// we still need to call buildChild() because we will get a
//				// reference to a node, and we need to clone it
////				childNode.addRight(buildChild(getRandomNode(parentNode2, r),
////						null, r, currentDepth + 1));
//				Node<Integer> nodeFromOtherParent = buildChild(getRandomNode(parentNode2, r),
//						null, r, currentDepth + 1);
//				childNode.setData(nodeFromOtherParent.getData());
//				childNode.setLeft(nodeFromOtherParent.getLeft());
//				childNode.setRight(nodeFromOtherParent.getRight());
//			}
			return childNode;
		}
		return null;
	}

	// if we reached max depth but a function follows, we add a random
	// terminal instead
	private static boolean maxAllowedDepthAndFunction(
			Node<Integer> parentNode1, int currentDepth) {
		return currentDepth == MAX_CHROMOSOME_DEPTH
				&& FunctionHelper.nodeIsFunction(parentNode1);
	}

	/*
	 * if the parent was never switched before (indicated by switchFlag = true)
	 * there is a random chance that this will return true and will change the
	 * flag to false if the parent was switched before (indicated by switchFlag
	 * = false) will return false
	 */
	private static boolean switchParent(Random r) {
		if (switchFlag && r.nextDouble() > 0.5d) {
			switchFlag = false;
			return true;
		}
		return false;
	}

	/*-
	 * Returns a REFERENCE to a random node in the following way: 
	 * - it randomly decides if it should continue, if not it returns the current node
	 * - if it continues, it randomly decides if it will go right or left (but only if those child exist)
	 * - if it reaches a leaf level, it will return the node above it
	 * - if parent has no children, it returns parent
	 */
	private static Node<Integer> getRandomNode(Node<Integer> parent, Random r) {
		int depth = 0;
		while (true) {
			boolean cont = false;
			// if it should continue
			if (r.nextDouble() > 0.4d) {
				if (r.nextDouble() < 0.5d && parent.getLeft() != null) {
					// go left
					parent = parent.getLeft();
					cont = true;
				} else if (parent.getRight() != null) {
					parent = parent.getRight();
					cont = true;
				}
			} else {
				parent.setDepth(depth);
				return parent;
			}
			if (!cont) {
				if (parent.getParent() != null) {
					parent.setDepth(depth - 1);
					return parent.getParent();
				}
				parent.setDepth(depth);
				return parent;
			}
			depth++;
		}
	}
}
