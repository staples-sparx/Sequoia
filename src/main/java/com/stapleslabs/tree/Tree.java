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
    private final TreeReducer reducer;

    @SuppressWarnings("unchecked")
    public Tree(List<Node<F>> nodes) {
        this.reducer = new TreeReducer();
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }


    public double reduceToValue(F features) {
        Node<F> node = nodes[0];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F> reduceToTree(F features, Set<IFeature> missingFeatures) {
        List<Node<F>> subTreeNodes = new ArrayList<>();

        reducer.reduceTree(0, nodes, features, missingFeatures, subTreeNodes);
        return new Tree<>(subTreeNodes);
    }

    public double[] optimizedReduceToValue(List<F> features, Set<IFeature> differingFeatures) {
        Path path = new Path();
        reducer.getFastPath(0, nodes, features.get(0), differingFeatures, path);
        int[][] fastPath = path.fastPath;
        double[] results = new double[features.size()];

        for (int i = 0; i < features.size(); ++i) {
            int currentIndex = path.root;
            Node<F> node = nodes[currentIndex];
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

    public Node<F>[] getNodes() {
        return nodes;
    }

}
