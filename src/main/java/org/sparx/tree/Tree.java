package org.sparx.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree<F, C> {

    private final Node<F, C>[] nodes;

    @SuppressWarnings("unchecked")
    public Tree(List<Node<F, C>> nodes) {
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }


    public double reduceToValue(C features) {
        Node<F, C> node = nodes[0];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F, C> reduceToTree(C features, Set<F> missingFeatures) {
        List<Node<F, C>> subTreeNodes = new ArrayList<>();

        TreeReducer.reduceTree(0, nodes, features, missingFeatures, subTreeNodes);
        return new Tree<>(subTreeNodes);
    }

    public double[] optimizedReduceToValue(List<C> features, Set<F> differingFeatures) {
        Path path = new Path();
        TreeReducer.getFastPath(0, nodes, features.get(0), differingFeatures, path);
        int[][] fastPath = path.fastPath;
        double[] results = new double[features.size()];

        for (int i = 0; i < features.size(); ++i) {
            int currentIndex = path.root;
            Node<F, C> node = nodes[currentIndex];
            while (!node.isLeaf) {

                int[] fastPathOffsets = fastPath[currentIndex];
                if (fastPathOffsets != null) {
                    currentIndex = node.nextNodeOffset(features.get(i), fastPathOffsets);
                } else {
                    currentIndex = node.nextNodeOffset(features.get(i));

                }
                node = nodes[currentIndex];
            }
            results[i] = node.value;
        }
        return results;
    }

    public Node<F, C>[] getNodes() {
        return nodes;
    }

}
