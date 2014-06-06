package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/15/14.
 */
public class TreeReducer {

    public <F> void reduceTree(int root, Node<F>[] nodes, F features, Set<IFeature> missingFeatures,
                                    List<Node<F>> subTreeNodes) {
        TIntStack nodesToSearchStack = new TIntArrayStack();
        TIntStack parentStack = new TIntArrayStack();
        int newRoot = subTreeNodes.size();

        Node<F> node = nodes[root];
        while (!node.isLeaf || nodesToSearchStack.size() != 0) {
            if (node.isLeaf) {
                int parentIndex = parentStack.pop();
                subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - newRoot);
                subTreeNodes.add(node.copyWithEmptyChildOffsets());
                node = nodes[root + nodesToSearchStack.pop()];
            } else if (missingFeatures.contains(node.feature)) {
                int newIndex = subTreeNodes.size();
                if (parentStack.size() != 0) {
                    int parentIndex = parentStack.pop();
                    subTreeNodes.get(parentIndex).addChildOffset(newIndex - newRoot);
                }

                subTreeNodes.add(node.copyWithEmptyChildOffsets());

                int[] childNodes = node.childOffsets;
                for (int i = childNodes.length - 1; i >= 0; --i) {
                    parentStack.push(newIndex);
                    nodesToSearchStack.push(childNodes[i]);
                }

                node = nodes[root + nodesToSearchStack.pop()];
            } else {
                node = nodes[root + node.nextNodeOffset(features)];
            }
        }
        if (parentStack.size() != 0) {
            int parentIndex = parentStack.pop();
            subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - newRoot);
        }
        subTreeNodes.add(node.copyWithEmptyChildOffsets());
    }
}
