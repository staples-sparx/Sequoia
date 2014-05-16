package com.stapleslabs.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Forest<F> {

    private final Node<F>[] nodes;
    private final int[] roots;

    @SuppressWarnings("unchecked")
    public Forest(List<Tree<F>> trees) {
        final int[] roots = new int[trees.size()];
        List<Node> forestNodes = new ArrayList<>();

        initializeRootsAndNodes(trees, roots, forestNodes);

        this.nodes = forestNodes.toArray(new Node[forestNodes.size()]);
        this.roots = roots;
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

    private void initializeRootsAndNodes(final List<Tree<F>> trees, final int[] roots, final List<Node> forestNodes) {
        int rootIndex = 0;
        for (int i = 0; i < trees.size(); i++) {
            Tree<F> tree = trees.get(i);
            Node[] nodes = tree.getNodes();
            roots[i] = rootIndex;
            rootIndex = rootIndex + nodes.length;

            Collections.addAll(forestNodes, nodes);
        }
    }
}
