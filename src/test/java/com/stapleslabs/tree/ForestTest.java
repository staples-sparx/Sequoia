package com.stapleslabs.tree;

import com.stapleslabs.utils.TestFeature;
import com.stapleslabs.utils.TestTrees;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ForestTest {

    private final List<Tree<TestFeature, Map<TestFeature, Integer>>> trees = new ArrayList<>();
    private Map<TestFeature, Integer> features;

    @Before
    public void setUp() {
        TestTrees treeGenerator = new TestTrees();
        for (int i = 0; i < 30; i++) {
            trees.add(treeGenerator.getRandomTree());
        }
        features = treeGenerator.getRandomFeatures();

    }

    @Test
    public void testReduceToValuesProducesSameValuesAsTrees() {
        List<Double> singleResults = new ArrayList<>();

        for (Tree<TestFeature, Map<TestFeature, Integer>> tree : trees) {
            singleResults.add(tree.reduceToValue(features));
        }

        Forest<TestFeature, Map<TestFeature, Integer>> forest = new Forest<>(trees);

        int counter = 0;
        for (double d : forest.reduceToValues(features)) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

    @Test
    public void testReduceToForestWithMissingFeatures() {
        Set<TestFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(TestFeature.CLASS_ID);
        missingFeatures.add(TestFeature.COG);
        missingFeatures.add(TestFeature.MONTH);

        Forest<TestFeature, Map<TestFeature, Integer>> forest = new Forest<>(trees);

        Forest<TestFeature, Map<TestFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertAccurateResults(subForest.reduceToValues(features));
    }

    @Test
    public void testReduceToForestWithoutMissingFeatures() {
        Set<TestFeature> missingFeatures = new HashSet<>();

        Forest<TestFeature, Map<TestFeature, Integer>> forest = new Forest<>(trees);

        Forest<TestFeature, Map<TestFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length == trees.size());

        assertAccurateResults(subForest.reduceToValues(features));
    }

    @Test
    public void testReduceToForestWithAllFeaturesMissing() {
        Set<TestFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(TestFeature.CLASS_ID);
        missingFeatures.add(TestFeature.COG);
        missingFeatures.add(TestFeature.MONTH);
        missingFeatures.add(TestFeature.DAY_OF_WEEK);
        missingFeatures.add(TestFeature.COST);
        missingFeatures.add(TestFeature.DISTANCE);

        Forest<TestFeature, Map<TestFeature, Integer>> forest = new Forest<>(trees);

        Forest<TestFeature, Map<TestFeature, Integer>> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length >= forest.getNodes().length);


        assertAccurateResults(subForest.reduceToValues(features));
    }

    @Test
    public void testOptimizedReduceToValuesProducesExpectedResults() {

        for (int i = 0; i < 1000; ++i) {

            Set<TestFeature> differingFeatures = new HashSet<>();
            differingFeatures.add(TestFeature.CLASS_ID);
            differingFeatures.add(TestFeature.COG);
            differingFeatures.add(TestFeature.MONTH);

            Forest<TestFeature, Map<TestFeature, Integer>> forest = new Forest<>(trees);

            double[][] results = forest.optimizedReduceToValues(Arrays.asList(features), differingFeatures);

            for (double[] result : results) {
                assertAccurateResults(result);
            }
        }


    }

    private void assertAccurateResults(double[] results) {

        List<Double> singleResults = new ArrayList<>();

        for (Tree<TestFeature, Map<TestFeature, Integer>> tree : trees) {
            singleResults.add(tree.reduceToValue(features));
        }

        int counter = 0;
        for (double d : results) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

}