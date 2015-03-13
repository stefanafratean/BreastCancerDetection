package util;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    //TODO regard this as a tree that contains a left subtree and a right subtree (which are also trees)
    //TODO the node class can be then removed
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

    @Override
    public boolean equals(Object obj) {
        //to maximize performance, we check in BFS order
        if (!(obj instanceof Tree)) {
            return false;
        }
        Tree<T> other = (Tree<T>) obj;
        List<Node<T>> toCheckFor1 = new ArrayList<Node<T>>();
        toCheckFor1.add(this.root);
        List<Node<T>> toCheckFor2 = new ArrayList<Node<T>>();
        toCheckFor2.add(other.root);

        return checkIfEqual(toCheckFor1, toCheckFor2);
    }

    private boolean checkIfEqual(List<Node<T>> toCheckFor1, List<Node<T>> toCheckFor2) {
        if (differentNumberOfSubTrees(toCheckFor1, toCheckFor2)) {
            return false;
        }
        if (nothingElseToCheck(toCheckFor1, toCheckFor2)) {
            return true;
        }

        Node<T> node1 = toCheckFor1.remove(toCheckFor1.size() - 1);
        Node<T> node2 = toCheckFor2.remove(toCheckFor2.size() - 1);

        if (bothNull(node1, node2)) {
            return true;
        } else if (exactlyOneNull(node1, node2)) {
            return false;
        }

        if (differentCurrentValues(node1, node2)) {
            return false;
        }

        toCheckFor1.add(0, node1.getLeft());
        toCheckFor1.add(0, node1.getRight());

        toCheckFor2.add(0, node2.getLeft());
        toCheckFor2.add(0, node2.getRight());

        // dupa ce verific subarborele drept, trebuie si stangul... folosim for?
        return checkIfEqual(toCheckFor1, toCheckFor2) && checkIfEqual(toCheckFor1, toCheckFor2);
    }

    private boolean differentCurrentValues(Node<T> node1, Node<T> node2) {
        return !node1.getData().equals(node2.getData());
    }

    private boolean exactlyOneNull(Node<T> node1, Node<T> node2) {
        return node1 == null || node2 == null;
    }

    private boolean bothNull(Node<T> node1, Node<T> node2) {
        return node1 == null && node2 == null;
    }

    private boolean nothingElseToCheck(List<Node<T>> toCheckFor1, List<Node<T>> toCheckFor2) {
        return toCheckFor1.isEmpty() && toCheckFor2.isEmpty();
    }

    private boolean differentNumberOfSubTrees(List<Node<T>> toCheckFor1, List<Node<T>> toCheckFor2) {
        return toCheckFor1.size() != toCheckFor2.size();
    }
}
