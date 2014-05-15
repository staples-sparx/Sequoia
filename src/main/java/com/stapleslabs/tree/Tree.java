package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

import java.util.List;
import java.util.Set;

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

    public Tree reduceToTree(F features, Set<IFeature> missingFeatures, List<Node<F>> newNodeList) {
        return reduceToTree(0, features, missingFeatures, newNodeList);
    }

    public Tree reduceToTree(int root, F missingFeatures, Set<IFeature> missing, List<Node<F>> newNodeList) {
        return null;
    }


}
