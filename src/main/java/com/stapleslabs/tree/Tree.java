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
        int currentIndex;
        while (!node.isLeaf) {
            currentIndex = root + node.nextNodeOffset(features);
            node = nodes[currentIndex];
        }
        return node.value;
    }

    public Tree<F> reduceToTree(F features, Set<IFeature> missingFeatures) {
        return reduceToTree(0, features, missingFeatures);
    }

    public Tree<F> reduceToTree(int root, F features, Set<IFeature> missingFeatures) {
        List<Node<F>> newNodeList = new ArrayList<>();
        TIntStack stack = new TIntArrayStack();
        TIntStack parentStack = new TIntArrayStack();

        Node<F> node = nodes[root];
        while (!node.isLeaf || stack.size() != 0) {
            if (node.isLeaf) {
                int parentIndex = parentStack.pop();
                newNodeList.get(parentIndex).addChildOffset(newNodeList.size() - parentIndex);
                newNodeList.add(node.copyWithEmptyChildOffsets());
                node = nodes[root + stack.pop()];
            } else if (missingFeatures.contains(node.feature)) {
                int newIndex = newNodeList.size();
                if (parentStack.size() != 0) {
                    int parentIndex = parentStack.pop();
                    newNodeList.get(parentIndex).addChildOffset(newNodeList.size() - parentIndex);
                }

                newNodeList.add(node.copyWithEmptyChildOffsets());

                int[] childNodes = node.childOffsets;
                for (int i = childNodes.length - 1; i >= 0; --i) {
                    parentStack.push(newIndex);
                    stack.push(childNodes[i]);
                }
                parentStack.pop();
                node = nodes[root + stack.pop()];
            } else {
                node = nodes[root + node.nextNodeOffset(features)];
            }
        }
        if (parentStack.size() != 0) {
            int parentIndex = parentStack.pop();
            newNodeList.get(parentIndex).addChildOffset(newNodeList.size() - parentIndex);
        }
        newNodeList.add(node);
        return new Tree<>(newNodeList);
    }

    public Node<F>[] getNodes() {
        return nodes;
    }

}
