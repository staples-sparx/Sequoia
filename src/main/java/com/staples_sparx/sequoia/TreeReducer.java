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

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;

import java.util.List;
import java.util.Set;

public final class TreeReducer {

    public static <F, C> void reduceTree(int root, Node<F, C>[] nodes, C features, Set<F> missingFeatures,
                                         List<Node<F, C>> subTreeNodes) {
        IntStack nodesToSearchStack = new IntArrayList();
        IntStack parentStack = new IntArrayList();
        int newRoot = subTreeNodes.size();

        Node<F, C> node = nodes[root];
        while (!node.isLeaf || !nodesToSearchStack.isEmpty()) {
            if (node.isLeaf) {
                int parentIndex = parentStack.popInt();
                subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - newRoot);
                subTreeNodes.add(node.copyWithEmptyChildOffsets());
                node = nodes[root + nodesToSearchStack.popInt()];
            } else if (missingFeatures.contains(node.feature)) {
                int newIndex = subTreeNodes.size();
                if (!parentStack.isEmpty()) {
                    int parentIndex = parentStack.popInt();
                    subTreeNodes.get(parentIndex).addChildOffset(newIndex - newRoot);
                }

                subTreeNodes.add(node.copyWithEmptyChildOffsets());

                int[] childNodes = node.childOffsets;
                for (int i = childNodes.length - 1; i >= 0; --i) {
                    parentStack.push(newIndex);
                    nodesToSearchStack.push(childNodes[i]);
                }

                node = nodes[root + nodesToSearchStack.popInt()];
            } else {
                node = nodes[root + node.nextNodeOffset(features)];
            }
        }
        if (!parentStack.isEmpty()) {
            int parentIndex = parentStack.popInt();
            subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - newRoot);
        }
        subTreeNodes.add(node.copyWithEmptyChildOffsets());
    }

}
