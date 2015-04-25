package com.stapleslabs.tree;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntStack;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/15/14.
 */
public class TreeReducer {

    public <F, C> void reduceTree(int root, Node<F, C>[] nodes, C features, Set<F> missingFeatures,
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

    public <F, C> void getFastPath(int root, Node<F, C>[] nodes, C features, Set<F> missingFeatures, Path path) {
        IntStack nodesToSearchStack = new IntArrayList();
        IntStack parentStack = new IntArrayList();
        int[][] fastPath = new int[nodes.length][];
        path.setFastPath(fastPath);
        int[] offsetState = new int[nodes.length];

        int currentIndex = root;
        Node<F, C> node = nodes[root];
        while (!node.isLeaf || !nodesToSearchStack.isEmpty()) {
            if (node.isLeaf) {
                int parentIndex = parentStack.popInt();
                int offsetIndex = offsetState[parentIndex];
                ++offsetState[parentIndex];
                int[] childOffsets = fastPath[parentIndex];
                childOffsets[offsetIndex] = currentIndex - childOffsets[offsetIndex];

                currentIndex = root + nodesToSearchStack.popInt();
                node = nodes[currentIndex];
            } else if (missingFeatures.contains(node.feature)) {
                if (!parentStack.isEmpty()) {
                    int parentIndex = parentStack.popInt();
                    int offsetIndex = offsetState[parentIndex];
                    ++offsetState[parentIndex];
                    int[] childOffsets = fastPath[parentIndex];
                    childOffsets[offsetIndex] = currentIndex - childOffsets[offsetIndex];
                } else {
                    path.setRoot(currentIndex);
                }

                int[] childNodeOffsets = node.childOffsets;
                for (int i = childNodeOffsets.length - 1; i >= 0; --i) {
                    parentStack.push(currentIndex);
                    nodesToSearchStack.push(childNodeOffsets[i]);
                }
                offsetState[currentIndex] = 0;
                fastPath[currentIndex] = Arrays.copyOf(childNodeOffsets, childNodeOffsets.length);

                currentIndex = root + nodesToSearchStack.popInt();
                node = nodes[currentIndex];
            } else {
                currentIndex = root + node.nextNodeOffset(features);
                node = nodes[currentIndex];
            }
        }
        if (!parentStack.isEmpty()) {
            int parentIndex = parentStack.popInt();
            int offsetIndex = offsetState[parentIndex];
            ++offsetState[parentIndex];
            int[] childOffsets = fastPath[parentIndex];
            childOffsets[offsetIndex] = currentIndex - childOffsets[offsetIndex];
        } else {
            path.setRoot(currentIndex);
        }
    }

}
