package util;

public class Node<T> {
    private T data;
    private Node<T> parent;
    private Node<T> left;
    private Node<T> right;
    private Node<T> middle;

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

    public Node<T> getMiddle() {
        return middle;
    }

    public void setMiddle(Node<T> middle) {
        this.middle = middle;
    }
}
