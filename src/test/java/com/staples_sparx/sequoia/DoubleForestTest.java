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

import org.junit.Before;
import org.junit.Test;
import com.staples_sparx.sequoia.utils.TestTrees;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by timbrooks on 6/7/15.
 */
public class DoubleForestTest {

    private final List<Tree<Integer, double[]>> trees = new ArrayList<>();
    private double[] features;

    @Before
    public void setUp() {
        TestTrees treeGenerator = new TestTrees();
        for (int i = 0; i < 30; i++) {
            trees.add(treeGenerator.getRandomTree(false));
        }
        features = treeGenerator.getRandomFeatures();
    }

    @Test
    public void testScoreTreesProducesSameValuesAsTrees() {
        List<Double> singleResults = new ArrayList<>();

        for (Tree<Integer, double[]> tree : trees) {
            singleResults.add(tree.scoreTree(features));
        }

        Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);
        DoubleCondition<double[]> condition = new DoubleCondition<double[]>() {
            @Override
            public int childOffset(double cutPoint, int feature, double[] features) {
                double value = features[feature];
                if (value == -1.0) {
                    return 0;
                } else if (value > cutPoint) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
        DoubleForest<double[]> doubleForest = DoubleForest.createFromForest(forest, condition);

        int counter = 0;
        for (double d : doubleForest.scoreTrees(features)) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }


}
