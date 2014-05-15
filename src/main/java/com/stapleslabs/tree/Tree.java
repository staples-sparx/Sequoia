package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import gnu.trove.stack.TIntStack;
import gnu.trove.stack.array.TIntArrayStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 5/14/14.
 */
public class Tree<F> {

    private final Node<F>[] nodes;

    @SuppressWarnings("unchecked")
    public Tree(List<Node<F>> nodes) {
        this.nodes = nodes.toArray(new Node[nodes.size()]);
    }

    public double reduceToValue(F features) {
        return reduceToValue(0, features);
    }

    public double reduceToValue(int root, F features) {
        Node<F> node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F> reduceToTree(F features, Set<IFeature> missingFeatures) {
        return reduceToTree(0, features, missingFeatures);
    }

    public Tree<F> reduceToTree(int root, F features, Set<IFeature> missingFeatures) {
        List<Node<F>> subTreeNodes = new ArrayList<>();
        TIntStack nodesToSearchStack = new TIntArrayStack();
        TIntStack parentStack = new TIntArrayStack();

        Node<F> node = nodes[root];
        while (!node.isLeaf || nodesToSearchStack.size() != 0) {
            if (node.isLeaf) {
                int parentIndex = parentStack.pop();
                subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - parentIndex);
                subTreeNodes.add(node.copyWithEmptyChildOffsets());
                node = nodes[root + nodesToSearchStack.pop()];
            } else if (missingFeatures.contains(node.feature)) {
                int newIndex = subTreeNodes.size();
                if (parentStack.size() != 0) {
                    int parentIndex = parentStack.pop();
                    subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - parentIndex);
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
            subTreeNodes.get(parentIndex).addChildOffset(subTreeNodes.size() - parentIndex);
        }
        subTreeNodes.add(node);
        return new Tree<>(subTreeNodes);
    }

    public Node<F>[] getNodes() {
        return nodes;
    }

}
