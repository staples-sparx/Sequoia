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
import java.util.List;
import java.util.Set;

public class DefaultForest<F, C> implements Forest<F, C> {

    private final Node<F, C>[] nodes;
    private final int[] roots;

    @SuppressWarnings("unchecked")
    private DefaultForest(List<Node<F, C>> nodes, int[] roots) {
        this(nodes.toArray(new Node[nodes.size()]), roots);
    }

    public DefaultForest(Node<F, C>[] nodes, int[] roots) {
        this.nodes = nodes;
        this.roots = roots;
    }

    @Override
    public double[] scoreTrees(C features) {
        double[] values = new double[roots.length];
        for (int i = 0; i < roots.length; i++) {
            values[i] = traverseSingleTree(roots[i], features);
        }
        return values;
    }

    @Override
    public Forest<F, C> reduceToForest(C features, Set<F> missingFeatures) {
        List<Node<F, C>> subForestNodes = new ArrayList<>();
        int[] newRoots = new int[roots.length];

        for (int i = 0; i < roots.length; ++i) {
            newRoots[i] = subForestNodes.size();
            TreeReducer.reduceTree(roots[i], nodes, features, missingFeatures, subForestNodes);
        }

        return new DefaultForest<>(subForestNodes, newRoots);
    }

    @Override
    public int[] getRoots() {
        return roots;
    }

    @Override
    public Node<F, C>[] getNodes() {
        return nodes;
    }

    private double traverseSingleTree(int root, C features) {
        Node<F, C> node = nodes[root];
        while (!node.isLeaf) {
            node = nodes[root + node.nextNodeOffset(features)];
        }
        return node.value;
    }
}
