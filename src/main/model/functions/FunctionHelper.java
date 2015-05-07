package model.functions;

import util.Node;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FunctionHelper {
     private List<Function> functions; // = Arrays.asList(plus, minus,
//            multiplier, safeDivision);

    public FunctionHelper(List<Function> functions) {
        this.functions = functions;
    }

    public boolean nodeIsFunction(Node<Integer> node) {
        return node.getData() < 0;
    }

    /*
     * returns a random value from the interval [-noOfFunctions; -1]
     */
    public int generateFunction(Random r) {
        int index = r.nextInt(functions.size()) + 1;
        index *= -1;
        return index;
    }

    /*
     * return the function mapped with index
     */
    public Function getFunction(int index) {
        int realIndex = -1 * index - 1;
        return functions.get(realIndex);
    }

}
