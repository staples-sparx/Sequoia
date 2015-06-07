package org.sparx.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree<F, C> {

    private final Node<F, C>[] nodes;

    public Tree(Node<F, C>[] nodes) {
        this.nodes = nodes;
    }


    public double scoreTree(C features) {
        Node<F, C> node = nodes[0];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F, C> reduceToTree(C features, Set<F> missingFeatures) {
        List<Node<F, C>> subTreeNodes = new ArrayList<>();

        TreeReducer.reduceTree(0, nodes, features, missingFeatures, subTreeNodes);
        return Planter.createTreeFromNodes(subTreeNodes);
    }

    public Node<F, C>[] getNodes() {
        return nodes;
    }

}
