package com.stapleslabs.tree;

import java.util.List;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Forest<F> {

    private final Node<F>[] nodes;
    private final int[] roots;

    public Forest(List<Tree<F>> trees) {
        this.nodes = null;
        this.roots = null;
    }

    public double[] reduceToValues(F features) {
        double[] values = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            values[i] = reduceToValue(roots[i], features);
        }
        return values;
    }

    private double reduceToValue(int root, F features) {
        Node<F> node = nodes[root];
        int currentIndex = root;
        while (!node.isLeaf) {
            currentIndex = currentIndex + node.nextNodeOffset(features);
            node = nodes[currentIndex];
        }
        return node.value;
    }
}
