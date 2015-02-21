package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Tree<T> {
	private final Node<T> root;

	public Tree(T data) {
		root = new Node<T>();
		root.setData(data);
	}

	public Tree(Node<T> node) {
		root = node;
	}

	public Node<T> getRoot() {
		return root;
	}

	// TODO remove and use Node.addLeft instead
	public Node<T> addLeft(Node<T> parent, T data) {
		Node<T> leaf = new Node<T>();
		leaf.setData(data);
		leaf.setParent(parent);
		parent.setLeft(leaf);

		return leaf;
	}

	// TODO remove
	public Node<T> addRight(Node<T> parent, T data) {
		Node<T> leaf = new Node<T>();
		leaf.setData(data);
		leaf.setParent(parent);
		parent.setRight(leaf);

		return leaf;
	}

	// TODO remove this and add toString()
	public void getString(Node<T> currentNode) {
		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new FileWriter("output.txt", true)))) {
			// rad
			out.print(currentNode.getData() + " ");

			// stg
			if (currentNode.getLeft() != null) {
				out.print(" l[ ");
				getString(currentNode.getLeft());
				out.print(" ] ");
			}

			// dr
			if (currentNode.getRight() != null) {
				out.print(" r[ ");
				getString(currentNode.getRight());
				out.print(" ] ");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
