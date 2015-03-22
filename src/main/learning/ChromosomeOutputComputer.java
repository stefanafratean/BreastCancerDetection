package learning;

import model.Chromosome;
import model.Radiography;
import model.functions.Function;
import model.functions.FunctionHelper;
import model.functions.TwoArgumentsFunction;
import util.Node;

public class ChromosomeOutputComputer {
    private TerminalOperator terminalOperator;

    public ChromosomeOutputComputer(TerminalOperator terminalOperator) {
        this.terminalOperator = terminalOperator;
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
}
