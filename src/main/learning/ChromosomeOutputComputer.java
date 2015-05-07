package learning;

import model.Chromosome;
import model.Radiography;
import model.functions.Function;
import model.functions.FunctionHelper;
import model.functions.ThreeArgumentsFunction;
import model.functions.TwoArgumentsFunction;
import util.Node;

public class ChromosomeOutputComputer {
    private TerminalOperator terminalOperator;
    private FunctionHelper functionHelper;

    public ChromosomeOutputComputer(TerminalOperator terminalOperator, FunctionHelper functionHelper) {
        this.terminalOperator = terminalOperator;
        this.functionHelper = functionHelper;
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
        if (!functionHelper.nodeIsFunction(node)) {
            return terminalOperator.getMappedValue(radiography, node.getData());
        } else {
            Function f = functionHelper.getFunction(node.getData());

            double leftSideResult = getOutputValue(node.getLeft(), radiography);
            double rightSideResult = getOutputValue(node.getRight(), radiography);

            if (f instanceof TwoArgumentsFunction) {
                return ((TwoArgumentsFunction) f).compute(leftSideResult, rightSideResult);
            } else {
                // three arguments function
                double middleSideResult = getOutputValue(node.getMiddle(), radiography);
                return ((ThreeArgumentsFunction) f).compute(leftSideResult, middleSideResult, rightSideResult);
            }
        }
    }
}
