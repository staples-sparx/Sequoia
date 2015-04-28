package com.stapleslabs.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Forest<F, C> {

    private final Node<F, C>[] nodes;
    private final int[] roots;

    @SuppressWarnings("unchecked")
    public Forest(List<Tree<F, C>> trees) {
        final int[] roots = new int[trees.size()];
        List<Node> forestNodes = new ArrayList<>();

        initializeRootsAndNodes(trees, roots, forestNodes);

        this.nodes = forestNodes.toArray(new Node[forestNodes.size()]);
        this.roots = roots;
    }

    @SuppressWarnings("unchecked")
    private Forest(List<Node<F, C>> nodes, int[] roots) {
        this.nodes = nodes.toArray(new Node[nodes.size()]);
        this.roots = roots;
    }

    public double[] reduceToValues(C features) {
        double[] values = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            values[i] = traverseSingleTree(roots[i], features);
        }
        return values;
    }

    public Forest<F, C> reduceToForest(C features, Set<F> missingFeatures) {
        List<Node<F, C>> subForestNodes = new ArrayList<>();
        int[] newRoots = new int[roots.length];

        for (int i = 0; i < roots.length; ++i) {
            newRoots[i] = subForestNodes.size();
            TreeReducer.reduceTree(roots[i], nodes, features, missingFeatures, subForestNodes);
        }

        return new Forest<>(subForestNodes, newRoots);
    }

    public double[][] optimizedReduceToValues(List<C> features, Set<F> differingFeatures) {
        double[][] results = new double[features.size()][];
        Path[] paths = new Path[roots.length];

        C firstFeatureMap = features.get(0);
        for (int i = 0; i < roots.length; ++i) {
            Path path = new Path();
            TreeReducer.getFastPath(roots[i], nodes, firstFeatureMap, differingFeatures, path);
            paths[i] = path;
        }


        for (int i = 0; i < features.size(); ++i) {
            results[i] = optimizedTraverseForest(features.get(i), paths);
        }


        return results;
    }

    public int[] getRoots() {
        return roots;
    }

    public Node<F, C>[] getNodes() {
        return nodes;
    }

    private double[] optimizedTraverseForest(C features, Path[] paths) {
        double[] results = new double[paths.length];
        for (int i = 0; i < paths.length; ++i) {
            Path path = paths[i];
            int[][] fastPath = path.fastPath;
            int currentIndex = path.root;
            Node<F, C> node = nodes[currentIndex];
            while (!node.isLeaf) {

                int[] fastPathOffsets = fastPath[currentIndex];
                if (fastPathOffsets != null) {
                    currentIndex = node.nextNodeOffset(features, fastPathOffsets);
                } else {
                    currentIndex = node.nextNodeOffset(features);

                }
                node = nodes[currentIndex];
            }
            results[i] = node.value;
        }
        return results;

    }

    private double traverseSingleTree(int root, C features) {
        Node<F, C> node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[root + node.nextNodeOffset(features)];
        }
        return node.value;
    }

    private void initializeRootsAndNodes(final List<Tree<F, C>> trees, final int[] roots, final List<Node> forestNodes) {
        int rootIndex = 0;
        for (int i = 0; i < trees.size(); i++) {
            Tree<F, C> tree = trees.get(i);
            Node[] nodes = tree.getNodes();
            roots[i] = rootIndex;
            rootIndex = rootIndex + nodes.length;

            Collections.addAll(forestNodes, nodes);
        }
    }
}
