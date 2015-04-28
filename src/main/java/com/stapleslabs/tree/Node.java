package com.stapleslabs.tree;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Node<F, C> {

        public final F feature;
        public final double value;
        public final boolean isLeaf;
        public final int[] childOffsets;
        private final ICondition<F, C> condition;
        private int currentChildIndex;

        public Node(F feature, double value, boolean isLeaf, int[] childOffsets, ICondition<F, C> condition) {
            this.feature = feature;
            this.value = value;
            this.isLeaf = isLeaf;
            this.childOffsets = childOffsets;
            this.condition = condition;
            this.currentChildIndex = 0;
        }

    public int nextNodeOffset(C features) {
        return childOffsets[condition.nextOffsetIndex(feature, features)];
    }

    public int nextNodeOffset(final C features, final int[] fastPathOffsets) {
        int index = condition.nextOffsetIndex(feature, features);
        return childOffsets[index] + fastPathOffsets[index];
    }

    public Node<F, C> copyWithEmptyChildOffsets() {
        return new Node<>(feature, value, isLeaf, new int[childOffsets.length], condition);
    }

    public void addChildOffset(int child) {
        childOffsets[currentChildIndex] = child;
        ++currentChildIndex;
    }
}
