package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import com.stapleslabs.utils.TestTrees;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ForestTest {

    private final List<Tree<Map<IFeature, Integer>>> trees = new ArrayList<>();
    private Map<IFeature, Integer> features;

    @Before
    public void setUp() {
        TestTrees treeGenerator = new TestTrees();
        for (int i = 0; i < 30; i ++) {
            trees.add(treeGenerator.getRandomTree());
        }
        features = treeGenerator.getRandomFeatures();

    }

    @Test
    public void testReduceToValuesProducesSameValuesAsTrees() {
        List<Double> singleResults = new ArrayList<>();

        for (Tree<Map<IFeature, Integer>> tree : trees) {
            singleResults.add(tree.reduceToValue(features));
        }

        Forest<Map<IFeature, Integer>> forest = new Forest<>(trees);

        int counter = 0;
        for (double d : forest.reduceToValues(features)) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

}