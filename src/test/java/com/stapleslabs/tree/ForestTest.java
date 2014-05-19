package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import com.stapleslabs.utils.TestTrees;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testReduceToForestWithMissingFeatures() {
        Set<IFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(TestTrees.Feature.CLASS_ID);
        missingFeatures.add(TestTrees.Feature.COG);
        missingFeatures.add(TestTrees.Feature.MONTH);

        Forest<Map<IFeature, Integer>> forest = new Forest<>(trees);

        Forest<Map<IFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertAccurateResults(subForest);
    }

    @Test
    public void testReduceToForestWithoutMissingFeatures() {
        Set<IFeature> missingFeatures = new HashSet<>();

        Forest<Map<IFeature, Integer>> forest = new Forest<>(trees);

        Forest<Map<IFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length == trees.size());

        assertAccurateResults(subForest);
    }

    @Test
    public void testReduceToForestWithAllFeaturesMissing() {
        Set<IFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(TestTrees.Feature.CLASS_ID);
        missingFeatures.add(TestTrees.Feature.COG);
        missingFeatures.add(TestTrees.Feature.MONTH);
        missingFeatures.add(TestTrees.Feature.DAY_OF_WEEK);
        missingFeatures.add(TestTrees.Feature.COST);
        missingFeatures.add(TestTrees.Feature.DISTANCE);

        Forest<Map<IFeature, Integer>> forest = new Forest<>(trees);

        Forest<Map<IFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length >= forest.getNodes().length);


        assertAccurateResults(subForest);
    }

    private void assertAccurateResults(final Forest<Map<IFeature, Integer>> subForest) {

        List<Double> singleResults = new ArrayList<>();

        for (Tree<Map<IFeature, Integer>> tree : trees) {
            singleResults.add(tree.reduceToValue(features));
        }

        int counter = 0;
        for (double d : subForest.reduceToValues(features)) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

}