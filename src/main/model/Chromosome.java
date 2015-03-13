package model;

import model.performancemeasure.PerformanceMeasure;
import util.Node;
import util.Tree;

import java.util.*;

import static model.functions.FunctionHelper.generateFunction;

/*
 *  Contains a tree with the mapping of the functions and terminals and the fitness
 *  The mapping is done from 0 to the size of the terminals list - 1
 *  and from -1 to the size of the functions list * (-1)
 */
public class Chromosome implements Comparable<Chromosome> {
    private final Tree<Integer> representation;
    private double fitness;
    private List<PerformanceMeasure> performanceMeasures;

    public Chromosome(Random r, List<PerformanceMeasure> measures) {
        this.performanceMeasures = measures;
        int f = generateFunction(r);
        representation = new Tree<Integer>(f);
    }

    public Chromosome(Tree<Integer> tree, List<PerformanceMeasure> measures) {
        representation = tree;
        this.performanceMeasures = measures;
    }

    private Chromosome(Tree<Integer> tree) {
        this.representation = tree;
    }

    public Node<Integer> getRootNode() {
        return representation.getRoot();
    }

    @Override
    public String toString() {
        return "" + fitness + performanceMeasures.toString();
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Tree<Integer> getRepresentation() {
        return representation;
    }


    @Override
    public int compareTo(Chromosome o) {
        if (fitness < o.fitness) {
            return -1;
        }
        if (fitness > o.fitness) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        //TODO this is not a great implementation, it would be better to check if the actual trees are equal
        if (!(obj instanceof Chromosome)) {
            return false;
        }
        Chromosome other = (Chromosome) obj;
//        for (int i = 0; i < this.performanceMeasures.size(); i++){
//            PerformanceMeasure measureForThis = this.performanceMeasures.get(i);
//            PerformanceMeasure measureForOther = other.performanceMeasures.get(i);
//            if (!measureForThis.equals(measureForOther)){
//                return false;
//            }
//        }
        return this.representation.equals(other.representation);
    }

    public Chromosome clone() throws CloneNotSupportedException {
        super.clone();
        Chromosome c = new Chromosome(representation);
        c.fitness = fitness;
        c.performanceMeasures = new ArrayList<PerformanceMeasure>();
        return c;
    }

    public int getDepth() {
        return getDepth(representation.getRoot());
    }

    private int getDepth(Node<Integer> node) {
        if (node.getLeft() == null && node.getRight() == null) {
            return 0;
        }
        int leftDepth = getDepth(node.getLeft()) + 1;
        int rightDepth = getDepth(node.getRight()) + 1;

        return Collections.max(Arrays.asList(leftDepth, rightDepth));
    }

    public List<PerformanceMeasure> getPerformanceMeasures() {
        return performanceMeasures;
    }
}
