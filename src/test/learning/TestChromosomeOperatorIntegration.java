package learning;

import java.util.Random;

import model.Chromosome;

import org.junit.Test;

import util.Node;
import static org.junit.Assert.*;

public class TestChromosomeOperatorIntegration {
	private Random r = new Random();
	private final static int MAX_DEPTH = 12;

	@Test
	public void testChromosomeIntegrityAfterInitialization() {
		for (int i = 0; i < 1000; i++) {
			Chromosome growChromosome = ChromosomeOperator.createChromosome(
					MAX_DEPTH, r, false);
			testIntegrity(growChromosome);
			Chromosome fullChromosome = ChromosomeOperator.createChromosome(
					MAX_DEPTH, r, true);
			testIntegrity(fullChromosome);
		}
	}

	@Test
	public void fullChromosomeRespectsSetLenght() {
		for (int i = 0; i < 1000; i++) {
			Chromosome fullChromosome = ChromosomeOperator.createChromosome(
					MAX_DEPTH, r, true);
			testSize(fullChromosome.getRootNode(), 0);
		}
	}

	@Test
	public void testOffSpringIntegrityAfterCrossover() {
		for (int i = 0; i < 10000; i++) {
			Chromosome mother = ChromosomeOperator.createChromosome(MAX_DEPTH,
					r, true);
			Chromosome father = ChromosomeOperator.createChromosome(MAX_DEPTH,
					r, false);
			Chromosome offspring = ChromosomeOperator.xo(mother, father, r);
			try{
				testIntegrity(offspring);
			}catch (AssertionError e){
			//	offspring.getString();
				throw e;
			}
		}
	}

	@Test
	public void testFullChromosomeIntegrityAfterMutation() {
		for (int i = 0; i < 10000; i++) {
			Chromosome c = ChromosomeOperator.createChromosome(MAX_DEPTH, r,
					true);
			c = ChromosomeOperator.mutation(c, r);
			testIntegrity(c);
		}
	}

	@Test
	public void testGrowChromosomeIntegrityAfterMutation() {
		for (int i = 0; i < 10000; i++) {
			Chromosome c = ChromosomeOperator.createChromosome(MAX_DEPTH, r,
					false);
			c = ChromosomeOperator.mutation(c, r);
			testIntegrity(c);
		}
	}

	private void testSize(Node<Integer> node, int currentDepth) {
		if (currentDepth < MAX_DEPTH) {
			assertNotNull(node.getLeft());
			assertNotNull(node.getRight());
			testSize(node.getLeft(), currentDepth + 1);
			testSize(node.getRight(), currentDepth + 1);
		} else {
			assertNull(node.getLeft());
			assertNull(node.getRight());
		}
	}

	private void testIntegrity(Chromosome chromosome) {
		Node<Integer> root = chromosome.getRepresentation().getRoot();

		// root must be function
		assertTrue(root.getData() < 0);

		testIntegrity(root.getLeft());
		testIntegrity(root.getRight());
	}

	private void testIntegrity(Node<Integer> node) {
		if (node.getData() >= 0) {
			// terminals should have no child
			assertNull(node.getLeft());
			assertNull(node.getRight());
		} else {
			// functions should have 2 childs
			assertNotNull(node.getLeft());
			assertNotNull(node.getRight());
			testIntegrity(node.getLeft());
			testIntegrity(node.getRight());
		}
	}

}
