package util;

public class Node<T> {
	private T data;
	private Node<T> parent;
	private Node<T> left;
	private Node<T> right;
	private int depth;

	public Node() {
		depth = 0;
	}

	public Node(T data, Node<T> parent, Node<T> left, Node<T> right) {
		this.data = data;
		this.parent = parent;
		this.left = left;
		this.right = right;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public Node<T> addLeft(Node<T> parent, T data) {
		Node<T> leaf = new Node<T>();
		leaf.setData(data);
		leaf.setParent(parent);
		parent.setLeft(leaf);

		return leaf;
	}

	public Node<T> addRight(Node<T> parent, T data) {
		Node<T> leaf = new Node<T>();
		leaf.setData(data);
		leaf.setParent(parent);
		parent.setRight(leaf);

		return leaf;
	}

	public void addLeft(Node<T> node) {
		this.left = node;
	}

	public void addRight(Node<T> node) {
		this.right = node;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth
	 *            the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
