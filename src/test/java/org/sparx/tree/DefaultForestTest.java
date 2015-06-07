package org.sparx.tree;

import org.junit.Before;
import org.junit.Test;
import org.sparx.tree.utils.TestFeature;
import org.sparx.tree.utils.TestTrees;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefaultForestTest {

    private final List<Tree<Integer, double[]>> trees = new ArrayList<>();
    private double[] features;

    @Before
    public void setUp() {
        TestTrees treeGenerator = new TestTrees();
        for (int i = 0; i < 30; i++) {
            trees.add(treeGenerator.getRandomTree(true));
        }
        features = treeGenerator.getRandomFeatures();

    }

    @Test
    public void testReduceToValuesProducesSameValuesAsTrees() {
        List<Double> singleResults = new ArrayList<>();

        for (Tree<Integer, double[]> tree : trees) {
            singleResults.add(tree.scoreTree(features));
        }

        Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);

        int counter = 0;
        for (double d : forest.scoreTrees(features)) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

    @Test
    public void testReduceToForestWithMissingFeatures() {
        Set<Integer> missingFeatures = new HashSet<>();
        missingFeatures.add(2);
        missingFeatures.add(3);
        missingFeatures.add(4);

        Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);

        Forest<Integer, double[]> subForest = forest.reduceToForest(features, missingFeatures);

        assertAccurateResults(subForest.scoreTrees(features));
    }

    @Test
    public void testReduceToForestWithoutMissingFeatures() {
        Set<Integer> missingFeatures = new HashSet<>();

        Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);

        Forest<Integer, double[]> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length == trees.size());

        assertAccurateResults(subForest.scoreTrees(features));
    }

    @Test
    public void testReduceToForestWithAllFeaturesMissing() {
        Set<Integer> missingFeatures = new HashSet<>();
        missingFeatures.add(0);
        missingFeatures.add(1);
        missingFeatures.add(2);
        missingFeatures.add(3);
        missingFeatures.add(4);
        missingFeatures.add(5);

        Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);

        Forest<Integer, double[]> subForest = forest.reduceToForest(features, missingFeatures);

        assertTrue(subForest.getNodes().length >= forest.getNodes().length);


        assertAccurateResults(subForest.scoreTrees(features));
    }

    @Test
    public void testOptimizedReduceToValuesProducesExpectedResults() {

        for (int i = 0; i < 1000; ++i) {

            Set<Integer> differingFeatures = new HashSet<>();
            differingFeatures.add(2);
            differingFeatures.add(3);
            differingFeatures.add(5);

            Forest<Integer, double[]> forest = Planter.createForestFromTrees(trees);

            double[][] results = forest.optimizedReduceToValues(Arrays.asList(features), differingFeatures);

            for (double[] result : results) {
                assertAccurateResults(result);
            }
        }


    }

    private void assertAccurateResults(double[] results) {

        List<Double> singleResults = new ArrayList<>();

        for (Tree<Integer, double[]> tree : trees) {
            singleResults.add(tree.scoreTree(features));
        }

        int counter = 0;
        for (double d : results) {
            assertEquals(singleResults.get(counter), d, 0.0);
            counter++;
        }
    }

}