/*
 * Copyright 2014 Staples Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.staples_sparx.sequoia;

import java.util.Arrays;

public class Node<F, C> {

    public final F feature;
    public final double value;
    public final boolean isLeaf;
    public final int[] childOffsets;
    private final Condition<F, C> condition;
    private int currentChildIndex;

    /**
     * Constructs an Node with the provided values.
     *
     * @param  feature the initial capacity of the list
     * @param  value  the initial capacity of the list
     * @param  isLeaf boolean flag indicating if node is a leaf
     * @param  childOffsets offsets from the tree root of the children nodes
     * @param  condition {@link Condition} condition that when evaluated will return the index of the child offset to
     *                                    visit next
     */
    public Node(F feature, double value, boolean isLeaf, int[] childOffsets, Condition<F, C> condition) {
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

    public Node<F, C> copyWithEmptyChildOffsets() {
        return new Node<>(feature, value, isLeaf, new int[childOffsets.length], condition);
    }

    public void addChildOffset(int child) {
        childOffsets[currentChildIndex] = child;
        ++currentChildIndex;
    }

    @Override
    public String toString() {
        return "Node{" +
                "feature=" + feature +
                ", value=" + value +
                ", isLeaf=" + isLeaf +
                ", childOffsets=" + Arrays.toString(childOffsets) +
                ", condition=" + condition +
                ", currentChildIndex=" + currentChildIndex +
                '}';
    }
}
