package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree {

    private final Node[] nodes;
    private final TreeReducer reducer;

    @SuppressWarnings("unchecked")
    public Tree(List<Node> nodes) {
        this.reducer = new TreeReducer();
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }

    public <F> double reduceToValue(F features) {
        return reduceToValue(0, features);
    }

    public <F> double reduceToValue(int root, F features) {
        Node node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public <F> Tree reduceToTree(F features, Set<IFeature> missingFeatures) {
        return reduceToTree(0, features, missingFeatures);
    }

    public <F> Tree reduceToTree(int root, F features, Set<IFeature> missingFeatures) {
        List<Node> subTreeNodes = new ArrayList<>();

        reducer.reduceTree(root, nodes, features, missingFeatures, subTreeNodes);
        return new Tree(subTreeNodes);
    }

    public Node[] getNodes() {
        return nodes;
    }

}
