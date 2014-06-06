package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Node {


        public final IFeature feature;
        public final double value;
        public final boolean isLeaf;
        public final int[] childOffsets;
        private final ICondition condition;
        private int currentChildIndex;

        public Node(IFeature feature, double value, boolean isLeaf, int[] childOffsets, ICondition condition) {
            this.feature = feature;
            this.value = value;
            this.isLeaf = isLeaf;
            this.childOffsets = childOffsets;
            this.condition = condition;
            this.currentChildIndex = 0;
        }

    public <F> int nextNodeOffset(F features) {
        return childOffsets[condition.nextOffsetIndex(feature, features)];
    }

    public Node copyWithEmptyChildOffsets() {
        return new Node(feature, value, isLeaf, new int[childOffsets.length], condition);
    }

    public void addChildOffset(int child) {
        childOffsets[currentChildIndex] = child;
        ++currentChildIndex;
    }
}
