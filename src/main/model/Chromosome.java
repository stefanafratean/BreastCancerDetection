package model;

import static model.functions.FunctionHelper.generateFunction;

import java.util.Random;

import util.Node;
import util.Tree;

/*
 *  Contains a tree with the mapping of the functions and terminals and the fitness
 *  The mapping is done from 0 to the size of the terminals list - 1
 *  and from -1 to the size of the functions list * (-1)
 */
public class Chromosome implements Comparable<Chromosome>{
	private final Tree<Integer> representation;
	private double fitness;

	public Chromosome(int maxDepth, Random r) {
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
		if (fitness < o.fitness){
			return 1;
		}
		if (fitness > o.fitness){
			return -1;
		}
		return 0;
	}
	
	public Chromosome clone() throws CloneNotSupportedException {
        super.clone();
		Chromosome c = new Chromosome(representation);
		c.fitness = fitness;
		return c;
	}
}
