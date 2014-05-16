package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree<F> {

    private final Node<F>[] nodes;
    private final TreeReducer<F> reducer;

    @SuppressWarnings("unchecked")
    public Tree(List<Node<F>> nodes) {
        this.reducer = new TreeReducer<>();
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }

    public double reduceToValue(F features) {
        return reduceToValue(0, features);
    }

    public double reduceToValue(int root, F features) {
        Node<F> node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F> reduceToTree(F features, Set<IFeature> missingFeatures) {
        return reduceToTree(0, features, missingFeatures);
    }

    public Tree<F> reduceToTree(int root, F features, Set<IFeature> missingFeatures) {
        List<Node<F>> subTreeNodes = new ArrayList<>();

        reducer.reduceTree(root, nodes, features, missingFeatures, subTreeNodes);
        return new Tree<>(subTreeNodes);
    }

    public Node<F>[] getNodes() {
        return nodes;
    }

}
