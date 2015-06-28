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

public class Tree<F, C> {

    private final Node<F, C>[] nodes;

    public Tree(Node<F, C>[] nodes) {
        this.nodes = nodes;
    }


    public double scoreTree(C features) {
        Node<F, C> node = nodes[0];
        while (!node.isLeaf) {
            node = nodes[node.nextNodeOffset(features)];
        }
        return node.value;
    }

    public Tree<F, C> reduceToTree(C features, Set<F> missingFeatures) {
        List<Node<F, C>> subTreeNodes = new ArrayList<>();

        TreeReducer.reduceTree(0, nodes, features, missingFeatures, subTreeNodes);
        return Planter.createTreeFromNodes(subTreeNodes);
    }

    public Node<F, C>[] getNodes() {
        return nodes;
    }

}
