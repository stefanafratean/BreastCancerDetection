package model;

import util.Node;
import util.Tree;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static model.functions.FunctionHelper.generateFunction;

/*
 *  Contains a tree with the mapping of the functions and terminals and the fitness
 *  The mapping is done from 0 to the size of the terminals list - 1
 *  and from -1 to the size of the functions list * (-1)
 */
public class Chromosome implements Comparable<Chromosome> {
    private final Tree<Integer> representation;
    private double fitness;
    private double wmw;

    public Chromosome(Random r) {
        int f = generateFunction(r);
        representation = new Tree<Integer>(f);
    }

    public Chromosome(Tree<Integer> tree) {
        representation = tree;
    }

    public Node<Integer> getRootNode() {
        return representation.getRoot();
    }

    @Override
    public String toString() {
        return "" + fitness;
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

    public Chromosome clone() throws CloneNotSupportedException {
        super.clone();
        Chromosome c = new Chromosome(representation);
        c.fitness = fitness;
        c.wmw = wmw;
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

    public double getWmw() {
        return wmw;
    }

    public void setWmw(double wmw) {
        this.wmw = wmw;
    }

}
