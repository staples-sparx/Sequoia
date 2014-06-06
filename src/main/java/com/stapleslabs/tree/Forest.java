package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Forest<F> {

    private final Node<F>[] nodes;
    private final int[] roots;
    private final TreeReducer reducer;

    @SuppressWarnings("unchecked")
    public Forest(List<Tree<F>> trees) {
        final int[] roots = new int[trees.size()];
        List<Node> forestNodes = new ArrayList<>();

        initializeRootsAndNodes(trees, roots, forestNodes);

        this.reducer = new TreeReducer();
        this.nodes = forestNodes.toArray(new Node[forestNodes.size()]);
        this.roots = roots;
    }

    @SuppressWarnings("unchecked")
    private Forest(List<Node<F>> nodes, int[] roots) {
        this.reducer = new TreeReducer();
        this.nodes = nodes.toArray(new Node[nodes.size()]);
        this.roots = roots;
    }

    public double[] reduceToValues(F features) {
        double[] values = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            values[i] = traverseSingleTree(roots[i], features);
        }
        return values;
    }

    public Forest<F> reduceToForest(F features, Set<IFeature> missingFeatures) {
        List<Node<F>> subForestNodes = new ArrayList<>();
        int[] newRoots = new int[roots.length];

        for (int i = 0; i < roots.length; ++i) {
            newRoots[i] = subForestNodes.size();
            reducer.reduceTree(roots[i], nodes, features, missingFeatures, subForestNodes);
        }

        return new Forest<>(subForestNodes, newRoots);
    }

    public Node<F>[] getNodes() {
        return nodes;
    }

    private double traverseSingleTree(int root, F features) {
        Node<F> node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[root + node.nextNodeOffset(features)];
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
