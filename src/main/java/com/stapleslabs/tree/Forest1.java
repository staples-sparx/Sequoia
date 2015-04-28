package com.stapleslabs.tree;

/**
 * Created by timbrooks on 4/28/15.
 */
public class Forest1 {

    private final int[] features;
    private final double[] values;
    private final boolean[] leafIndicators;
    private final int[] offsets;
    private final double[] cutPoints;
    private final int[] roots;
    private final SpecializedCondition condition;

    public Forest1(int[] features, double[] values, boolean[] leafIndicators, int[] offsets, double[] cutPoints,
                   int[] roots, SpecializedCondition condition) {
        this.features = features;
        this.values = values;
        this.leafIndicators = leafIndicators;
        this.offsets = offsets;
        this.cutPoints = cutPoints;
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

    public static <C> Forest1 createFromForest(Forest<Integer, C> forest, double[] cutPoints) {
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
        return new Forest1(features, values, leafIndicators, offsets, cutPoints, roots, new SpecializedCondition());
    }

    private double traverseSingleTree(int root, double[] features) {
        int node = root;
        while (!leafIndicators[root]) {
            double cutPoint = cutPoints[root];
            int childOffset = condition.childOffset(cutPoint, this.features[node], features);
            int offset = (childOffset == 1) ? offsets[node * 2] : offsets[node * 2 + 1];
            node = root + offset;
        }
        return values[node];
    }


}
