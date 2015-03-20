package util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest {
    private final static String LEFT = "LEFT";
    private final static String RIGHT = "RIGHT";
    private final static String ROOT = "ROOT";

    @Test
    public void identical_trees_are_equal() {
        Tree<String> tree1 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Tree<String> tree2 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);

        assertTrue(tree1.equals(tree2));
    }

    @Test
    public void trees_with_different_root_are_not_equal() {
        Tree<String> tree1 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Tree<String> tree2 = createTreeWithHeightOne("otherRoot", LEFT, RIGHT);

        assertFalse(tree1.equals(tree2));
    }

    @Test
    public void complete_trees_with_different_leafs_are_not_equal() {
        Tree<String> tree1 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Tree<String> tree2 = createTreeWithHeightOne(ROOT, LEFT, "otherRight");

        assertFalse(tree1.equals(tree2));
    }

    @Test
    public void incomplete_trees_with_different_leafs_are_not_equal() {
        Tree<String> tree1 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Tree<String> tree2 = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Node<String> node = new Node<String>();
        node.setData("data");
        tree2.getRoot().getRight().setRight(node);

        assertFalse(tree1.equals(tree2));
    }

    private Tree<String> createTreeWithHeightOne(String rootData, String leftData, String rightData) {
        Node<String> root = createNode(rootData);
        Node<String> left = createNode(leftData);
        Node<String> right = createNode(rightData);

        root.setLeft(left);
        root.setRight(right);

        return new Tree<String>(root);
    }

    private Node<String> createNode(String data) {
        Node<String> right = new Node<String>();
        right.setData(data);
        return right;
    }

    @Test
    public void tree_with_one_has_height_zero() {
        Tree<String> tree = new Tree<String>(new Node<String>());

        assertEquals(0, tree.getHeight());
    }

    @Test
    public void height_correctly_computed_for_complete_tree() {
        Tree<String> tree = createTreeWithHeightOne(ROOT, LEFT, RIGHT);

        assertEquals(1, tree.getHeight());
    }

    @Test
    public void height_correctly_computed_for_incomplete_tree() {
        Tree<String> tree = createTreeWithHeightOne(ROOT, LEFT, RIGHT);
        Node<String> node = new Node<String>();
        tree.getRoot().getRight().setRight(node);

        assertEquals(2, tree.getHeight());
    }
}