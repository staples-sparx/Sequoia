package org.sparx.tree;

import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 4/28/15.
 */
public class Forest1 implements Forest<Integer, double[]> {

    private final int[] features;
    private final double[] values;
    private final boolean[] leafIndicators;
    private final int[] offsets;
    private final int[] roots;
    private final SpecializedCondition condition;

    public Forest1(int[] features, double[] values, boolean[] leafIndicators, int[] offsets, int[] roots,
                   SpecializedCondition condition) {
        this.features = features;
        this.values = values;
        this.leafIndicators = leafIndicators;
        this.offsets = offsets;
        this.roots = roots;
        this.condition = condition;
    }

    public double[] reduceToValues(double[] features) {
        double[] values = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            values[i] = traverseSingleTree(roots[i], features);
        }
        return values;
    }

    @Override
    public Forest<Integer, double[]> reduceToForest(double[] features, Set<Integer> missingFeatures) {
        throw new UnsupportedOperationException("Not allowed");
    }

    @Override
    public double[][] optimizedReduceToValues(List<double[]> features, Set<Integer> differingFeatures) {
        throw new UnsupportedOperationException("Not allowed");
    }

    @Override
    public int[] getRoots() {
        return roots;
    }

    @Override
    public Node<Integer, double[]>[] getNodes() {
        throw new UnsupportedOperationException("Not allowed");
    }

    public static <C> Forest1 createFromForest(Forest<Integer, C> forest) {
        int maxChildren = 2;

        Node<Integer, C>[] nodes = forest.getNodes();

        int[] features = new int[nodes.length];
        double[] values = new double[nodes.length];
        boolean[] leafIndicators = new boolean[nodes.length];
        int[] offsets = new int[nodes.length * maxChildren];
        int[] roots = forest.getRoots();
        int i = 0;
        for (Node<Integer, C> node : nodes) {
            features[i] = node.feature;
            values[i] = node.value;
            leafIndicators[i] = node.isLeaf;
            int[] childOffsets = node.childOffsets;
            int j = 0;
            for (int childOffset : childOffsets) {
                offsets[(i * maxChildren) + j] = childOffset;
                ++j;
            }
            ++i;
        }
        return new Forest1(features, values, leafIndicators, offsets, roots, new SpecializedCondition());
    }

    private double traverseSingleTree(int root, double[] features) {
        int node = root;
        while (!leafIndicators[node]) {
            double cutPoint = values[node];
            int childOffset = condition.childOffset(cutPoint, this.features[node], features);
            int offset = (childOffset == 0) ? offsets[node * 2] : offsets[node * 2 + 1];
            node = root + offset;
        }
        return values[node];
    }


}
