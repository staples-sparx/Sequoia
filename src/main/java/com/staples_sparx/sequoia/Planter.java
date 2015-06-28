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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
