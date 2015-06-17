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
