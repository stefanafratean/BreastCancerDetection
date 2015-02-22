package util;

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
}
