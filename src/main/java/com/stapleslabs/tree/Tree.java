package com.stapleslabs.tree;

import java.util.List;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree<F> {

    private final Node<F>[] nodes;

    @SuppressWarnings("unchecked")
    public Tree(List<Node<F>> nodes) {
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }

    public double reduceToValue(F features) {
        return reduceToValue(0, features);
    }

    public double reduceToValue(int root, F features) {
        Node<F> node = nodes[root];
        int currentIndex = root;
        while (!node.isLeaf) {
            currentIndex = currentIndex + node.nextNodeOffset(features);
            node = nodes[currentIndex];
        }
        return node.value;
    }


}
