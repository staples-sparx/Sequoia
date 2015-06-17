package com.staples_sparx.sequoia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by timbrooks on 4/28/15.
 */
public final class Planter {

    /**
     * Construct a {@link DefaultForest} from the list of {@link Tree} objects.
     *
     * @param trees {@link List<Tree>} trees that will compose the forest.
     * @return a forest
     */
    @SuppressWarnings("unchecked")
    public static <F, C> Forest<F, C> createForestFromTrees(List<Tree<F, C>> trees) {
        final int[] roots = new int[trees.size()];
        List<Node> forestNodes = new ArrayList<>();
        int rootIndex = 0;
        for (int i = 0; i < trees.size(); i++) {
            Tree<F, C> tree = trees.get(i);
            Node[] nodes = tree.getNodes();
            roots[i] = rootIndex;
            rootIndex = rootIndex + nodes.length;

            Collections.addAll(forestNodes, nodes);
        }

        Node<F, C>[] nodes = forestNodes.toArray(new Node[forestNodes.size()]);
        return new DefaultForest<>(nodes, roots);
    }

    /**
     * Construct a {@link Tree} from the list of {@link Node} objects.
     *
     * @param nodes {@link List<Node>} nodes that will compose the tree.
     * @return a tree
     */
    @SuppressWarnings("unchecked")
    public static <F, C> Tree<F, C> createTreeFromNodes(List<Node<F, C>> nodes) {
        return new Tree<>(nodes.toArray(new Node[nodes.size()]));
    }
}
