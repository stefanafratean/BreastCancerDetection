package learning;

import fitness.PerformanceCalculator;
import model.Chromosome;
import model.Radiography;
import model.functions.Function;
import model.functions.FunctionHelper;
import model.functions.TwoArgumentsFunction;
import model.performancemeasure.PerformanceMeasure;
import util.MathUtil;
import util.Node;
import util.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static model.functions.FunctionHelper.generateFunction;

public class ChromosomeOperator {
    private boolean switchFlag;
    private TerminalOperator terminalOperator;
    public final int MAX_CHROMOSOME_DEPTH;

    public ChromosomeOperator(TerminalOperator terminalOperator) {
        this.terminalOperator = terminalOperator;
        MAX_CHROMOSOME_DEPTH = (int) (Math.round(Math
                .log(terminalOperator.getNumberOfTerminals()) / Math.log(2)));
    }

    public Chromosome xo(List<PerformanceCalculator> calculators, Chromosome mother, Chromosome father, Random r) {

        Node<Integer> node = setFlagAndBuildChild(mother, father, r);
        Tree<Integer> tree = new Tree<Integer>(node);

        return new Chromosome(tree, getNewPerformanceMeasures(calculators));
    }

    private List<PerformanceMeasure> getNewPerformanceMeasures(List<PerformanceCalculator> calculators) {
        List<PerformanceMeasure> performanceMeasures = new ArrayList<PerformanceMeasure>();
        for (PerformanceCalculator calculator : calculators) {
            PerformanceMeasure measure = new PerformanceMeasure(calculator, 0);
            performanceMeasures.add(measure);
        }
        return performanceMeasures;
    }

    public Chromosome mutation(Chromosome chromosome, Random r) {
        Node<Integer> node = getRandomNode(chromosome.getRepresentation()
                .getRoot(), r);
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
     * @param maxDepth represents the maximum allowed depth of the chromosome
     * @param r        random number used to choose terminals and functions
     * @param isFull   should be set to true if we want the chromosome to be
     *                 initializes with the full method, false if we want it to be
     *                 initialized with the grow method.
     * @return
     */
    public Chromosome createChromosome(List<PerformanceCalculator> calculators, int maxDepth, Random r,
                                       boolean isFull) {
        Chromosome chromosome = new Chromosome(r, getNewPerformanceMeasures(calculators));
        initChromosome(maxDepth, 0, r, chromosome.getRootNode(), isFull);
        return chromosome;
    }

    private void initChromosome(int maxDepth, int currentDepth,
                                Random r, Node<Integer> currentNode, boolean isFull) {

        if (addTerminalAndCheckIfFull(maxDepth, currentDepth, r, currentNode, isFull)) return;
        addFunction(r, currentNode);
        initializeLeft(maxDepth, currentDepth, r, currentNode, isFull);
        initializeRight(maxDepth, currentDepth, r, currentNode, isFull);
    }

    private void initializeRight(int maxDepth, int currentDepth, Random r, Node<Integer> currentNode, boolean isFull) {
        Node<Integer> rightNode = new Node<Integer>();
        currentNode.setRight(rightNode);
        rightNode.setParent(currentNode);
        initChromosome(maxDepth, currentDepth + 1, r, rightNode, isFull);
    }

    private void initializeLeft(int maxDepth, int currentDepth, Random r, Node<Integer> currentNode, boolean isFull) {
        Node<Integer> leftNode = new Node<Integer>();
        currentNode.setLeft(leftNode);
        leftNode.setParent(currentNode);
        initChromosome(maxDepth, currentDepth + 1, r, leftNode, isFull);
    }

    private void addFunction(Random r, Node<Integer> currentNode) {
        currentNode.setData(generateFunction(r));
    }

    private boolean addTerminalAndCheckIfFull(int maxDepth, int currentDepth, Random r, Node<Integer> currentNode, boolean isFull) {
        if (currentDepth != 0 && (terminalOperator.shouldAddTerminal(r, isFull) || currentDepth == maxDepth)) {
            currentNode.setData(terminalOperator.generateTerminal(r));
            return true;
        }
        return false;
    }

    /**
     * Returns a number that is the result of the discriminant function
     * represented by the chromosome
     */
    public double getOutputValue(Chromosome chromosome,
                                 Radiography radiography) {
        double rawValue = getOutputValue(chromosome.getRepresentation().getRoot(),
                radiography);
//        return MathUtil.sigmoid(rawValue);
        return rawValue;
    }

    private double getOutputValue(Node<Integer> node,
                                  Radiography radiography) {
        if (!FunctionHelper.nodeIsFunction(node)) {
            return terminalOperator.getMappedValue(radiography, node.getData());
        } else {
            Function f = FunctionHelper.getFunction(node.getData());

            double leftSideResult = getOutputValue(node.getLeft(), radiography);
            double rightSideResult = getOutputValue(node.getRight(), radiography);
            return ((TwoArgumentsFunction) f).compute(leftSideResult, rightSideResult);
        }
    }

    private int getDifferentTerminal(int current, Random r) {
        int newTerminal;
        while (true) {
            newTerminal = terminalOperator.generateTerminal(r);
            if (current != newTerminal) {
                return newTerminal;
            }
        }
    }

    private int getDifferentFunction(int current, Random r) {
        int newTerminal;
        while (true) {
            newTerminal = FunctionHelper.generateFunction(r);
            if (current != newTerminal) {
                return newTerminal;
            }
        }
    }

    private Node<Integer> setFlagAndBuildChild(Chromosome mother,
                                               Chromosome father, Random r) {
        switchFlag = true;
        Node<Integer> node = buildChild(mother.getRepresentation().getRoot(),
                father.getRepresentation().getRoot(), r, 0);
        switchFlag = true;
        return node;
    }

    private Node<Integer> buildChild(Node<Integer> parentNode1,
                                     Node<Integer> parentNode2, Random r, int currentDepth) {
        if (parentNode1 != null) {
            Node<Integer> childNode = new Node<Integer>();
            childNode.setParent(parentNode1.getParent());
            if (maxAllowedDepthAndFunction(parentNode1, currentDepth)) {
                childNode.setData(terminalOperator.generateTerminal(r));
                childNode.setLeft(null);
                childNode.setRight(null);
                return childNode;
            }
            childNode.setData(parentNode1.getData());
            // TODO see if can use lambda expr instead
            if (currentDepth == 0 || !switchParent(r)) {
                //	if (FunctionHelper.nodeIsFunction(childNode)){
                childNode.setLeft(buildChild(parentNode1.getLeft(),
                        parentNode2, r, currentDepth + 1));
                childNode.setRight(buildChild(parentNode1.getRight(),
                        parentNode2, r, currentDepth + 1));
                //	}
            } else {
                Node<Integer> nodeFromOtherParent = buildChild(getRandomNode(parentNode2, r),
                        null, r, currentDepth + 1);
                childNode.setData(nodeFromOtherParent.getData());
                childNode.setLeft(nodeFromOtherParent.getLeft());
                childNode.setRight(nodeFromOtherParent.getRight());
            }
            return childNode;
        }
        return null;
    }

    // if we reached max depth but a function follows, we add a random
    // terminal instead
    private boolean maxAllowedDepthAndFunction(
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
    private boolean switchParent(Random r) {
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
    private Node<Integer> getRandomNode(Node<Integer> parent, Random r) {
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
                return parent;
            }
            if (!cont) {
                if (parent.getParent() != null) {
                    return parent.getParent();
                }
                return parent;
            }
            depth++;
        }
    }
}
