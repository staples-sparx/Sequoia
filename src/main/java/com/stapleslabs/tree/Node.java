package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Node<T> {


        public final IFeature feature;
        public final double value;
        public final boolean isLeaf;
        public final int[] childOffsets;
        private final ICondition<T> condition;
        private int currentChildIndex;

        public Node(IFeature feature, double value, boolean isLeaf, int[] childOffsets, ICondition<T> condition) {
            this.feature = feature;
            this.value = value;
            this.isLeaf = isLeaf;
            this.childOffsets = childOffsets;
            this.condition = condition;
            this.currentChildIndex = 0;
        }

    public int nextNodeOffset(T features) {
        return childOffsets[condition.nextOffsetIndex(feature, features)];
    }
}
