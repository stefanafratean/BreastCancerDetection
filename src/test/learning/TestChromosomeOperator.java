package learning;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import model.Chromosome;

import org.junit.Before;
import org.junit.Test;

import util.Node;
import util.Tree;

public class TestChromosomeOperator {
	private Chromosome chromosome;
	private Random r;
	private final static double BIGGER = 0.6;
	private final static double SMALLER = 0.3;

	@Before
	public void setUp() {
		chromosome = mock(Chromosome.class);
		r = mock(Random.class);
	}

	/*-
	 * father:                  mother:                     child:
	 *         root                    root2                root2
	 *       /     \                   /   \                /   \
	 *      l       r                 l2   r2              l2   rl
	 *            /  \                                         / \
	 *    -2=>  rl    rr                                     rll  rlr
	 *         /  \ 
	 *       rll  rlr
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testChildCorrectlyGenerated() {
		Node<Integer> rl = initializeChromozomeAndGetRealNode(true);
		when(r.nextDouble()).thenReturn(SMALLER).thenReturn(SMALLER)
				.thenReturn(SMALLER).thenReturn(BIGGER).thenReturn(BIGGER)
				.thenReturn(BIGGER).thenReturn(BIGGER).thenReturn(SMALLER)
				.thenReturn(SMALLER);

		Node<Integer> root2 = mock(Node.class);
		Node<Integer> l2 = mock(Node.class);
		Node<Integer> r2 = mock(Node.class);
		setChilds(root2, l2, r2);
		setChilds(l2, null, null);
		setChilds(r2, null, null);
		Tree<Integer> tree = mock(Tree.class);
		when(tree.getRoot()).thenReturn(root2);
		Chromosome chromosome2 = mock(Chromosome.class);
		when(chromosome2.getRepresentation()).thenReturn(tree);

		Chromosome child = ChromosomeOperator.xo(chromosome2, chromosome, r);

		Node<Integer> node = child.getRepresentation().getRoot().getRight();
		assertEquals(1, (int) node.getData());

		// reference to parents should not be kept, modifications on parent
		// shouldn't affect child
		rl.setData(10);
		assertEquals(1, (int) node.getData());
	}

	private void setChilds(Node<Integer> parent, Node<Integer> left,
			Node<Integer> right) {
		when(parent.getLeft()).thenReturn(left);
		when(parent.getRight()).thenReturn(right);
	}

	@SuppressWarnings("unchecked")
	private Node<Integer> initializeChromozomeAndGetRealNode(
			boolean realNodeHasChildren) {
		Node<Integer> root = mock(Node.class);
		Node<Integer> l = mock(Node.class);
		Node<Integer> r = mock(Node.class);
		Node<Integer> rr = mock(Node.class);

		Node<Integer> rl = new Node<Integer>();
		rl.setParent(r);
		rl.setData(1);
		when(r.getData()).thenReturn(1);
		if (realNodeHasChildren) {
			Node<Integer> rll = mock(Node.class);
			Node<Integer> rlr = mock(Node.class);
			setChilds(rll, null, null);
			setChilds(rlr, null, null);
			rl.setLeft(rll);
			rl.setRight(rlr);
		} else {
			rl.setLeft(null);
			rl.setRight(null);
		}

		setChilds(root, l, r);
		setChilds(l, null, null);
		setChilds(r, rl, rr);
		setChilds(rr, null, null);

		Tree<Integer> tree = mock(Tree.class);
		when(tree.getRoot()).thenReturn(root);
		when(chromosome.getRepresentation()).thenReturn(tree);

		return rl;
	}
}
