package com.stapleslabs.tree;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 4/28/15.
 */
public class Forest2 implements Forest<Integer, double[]> {

    private final ByteBuffer newForest;
    private final int[] roots;
    private final SpecializedCondition condition;

    public Forest2(ByteBuffer newForest, int[] roots, SpecializedCondition condition) {
        this.newForest = newForest;
        this.roots = roots;
        this.condition = condition;
    }


    @Override
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

    public static <C> Forest2 createFromForest(Forest<Integer, C> forest, double[] cutPoints) {
        Node<Integer, C>[] nodes = forest.getNodes();
        int maxChildren = 2;

        int nodeSize =  2 + 4 + 8 + (maxChildren * 4) + 8;
        ByteBuffer newForest = ByteBuffer.allocate(nodeSize * nodes.length);

        int[] roots = forest.getRoots();
        int i = 0;
        for (Node<Integer, C> node : nodes) {
            int offset = i * 22;
            short isLeaf = 1;
            short isNotLeaf = 0;
            newForest.putShort(node.isLeaf ? isLeaf : isNotLeaf);
            newForest.putInt(node.feature);
            newForest.putDouble(cutPoints[i]);
            int[] childOffsets = node.childOffsets;
            for (int childOffset : childOffsets) {
                newForest.putInt(childOffset);
            }
            newForest.putDouble(node.value);
            ++i;
        }
        return new Forest2(newForest, roots, new SpecializedCondition());
    }

    private double traverseSingleTree(int root, double[] features) {
        int nodeOffset = root * 30;
        while (newForest.getShort(nodeOffset) != 1) {
            int feature = newForest.getInt(nodeOffset + 2);
            double cutPoint = newForest.getDouble(nodeOffset + 6);
            int childOffset = condition.childOffset(cutPoint, feature, features);
            int offset = (childOffset == 0) ? newForest.getInt(nodeOffset + 14) : newForest.getInt(nodeOffset + 18);
            nodeOffset = root + (offset * 30);
        }
        return newForest.getDouble(nodeOffset + 22);
    }
}
