package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TreeTest {

    private Tree<HashMap<IFeature, Integer>> tree;

    @Before
    public void setUp() {
        int[] childChildOffsets = {-1, -1};

        int[] nodeZeroChildOffsets = {1, 2};
        Node<HashMap<IFeature, Integer>> nodeZero = new Node<>(Feature.COST, -1.0, false, nodeZeroChildOffsets, new Numeric(23));

        int[] nodeOneChildOffsets = {5, 3};
        HashMap<Integer, Integer> dayOfWeekMappings = new HashMap<>();
        dayOfWeekMappings.put(0, 0);
        dayOfWeekMappings.put(1, 0);
        dayOfWeekMappings.put(2, 1);
        Node<HashMap<IFeature, Integer>> nodeOne = new Node<>(Feature.DAY_OF_WEEK, -1.0, false, nodeOneChildOffsets, new Categorical(dayOfWeekMappings));

        int[] nodeTwoChildOffsets = {3, 4};
        HashMap<Integer, Integer> monthMappings = new HashMap<>();
        monthMappings.put(0, 1);
        monthMappings.put(1, 1);
        monthMappings.put(2, 0);
        monthMappings.put(3, 1);
        Node<HashMap<IFeature, Integer>> nodeTwo = new Node<>(Feature.MONTH, -1.0, false, nodeTwoChildOffsets, new Categorical(monthMappings));

        Node<HashMap<IFeature, Integer>> nodeThree = new Node<>(null, 5.0, true, childChildOffsets, null);
        Node<HashMap<IFeature, Integer>> nodeFour = new Node<>(null, 10.0, true, childChildOffsets, null);
        Node<HashMap<IFeature, Integer>> nodeFive = new Node<>(null, 15.0, true, childChildOffsets, null);

        tree = new Tree<>(Arrays.asList(nodeZero, nodeOne, nodeTwo, nodeThree, nodeFour, nodeFive));
    }

    @Test
    public void testReduceToValue() {
        HashMap<IFeature, Integer> features = new HashMap<>();
        assertEquals(15.0, tree.reduceToValue(features), 0.0);

        features.put(Feature.COST, 24);
        assertEquals(5.0, tree.reduceToValue(features), 0.0);

        features.put(Feature.MONTH, 2);
        assertEquals(5.0, tree.reduceToValue(features), 0.0);

        features.put(Feature.MONTH, 1);
        assertEquals(10.0, tree.reduceToValue(features), 0.0);

        features.put(Feature.COST, 22);
        features.put(Feature.DAY_OF_WEEK, 1);
        assertEquals(15.0, tree.reduceToValue(features), 0.0);

        features.put(Feature.DAY_OF_WEEK, 2);
        assertEquals(5.0, tree.reduceToValue(features), 0.0);
    }

    @Test
    public void testReduceToSubTree() {
        Set<IFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(Feature.DAY_OF_WEEK);
        HashMap<IFeature, Integer> features = new HashMap<>();
        features.put(Feature.COST, 22);

        Tree<HashMap<IFeature, Integer>> subTree = tree.reduceToTree(features, missingFeatures);

        assertEquals(3, subTree.getNodes().length);
        assertEquals(15.0, subTree.reduceToValue(features), 0.0);
    }

    @Test
    public void testReduceToSubTreeProducesSingleNodeIfAppropriate() {
        Set<IFeature> missingFeatures = new HashSet<>();
        missingFeatures.add(Feature.DAY_OF_WEEK);
        HashMap<IFeature, Integer> features = new HashMap<>();
        features.put(Feature.COST, 24);

        Tree<HashMap<IFeature, Integer>> singleNode = tree.reduceToTree(features, missingFeatures);
        
        assertEquals(1, singleNode.getNodes().length);
        assertEquals(5.0, singleNode.reduceToValue(features), 0.0);
    }

    private enum Feature implements IFeature {
        COST,
        MONTH,
        DAY_OF_WEEK
    }

    private static class Categorical implements ICondition<HashMap<IFeature, Integer>> {

        private final HashMap<Integer, Integer> conditions;

        public Categorical(HashMap<Integer, Integer> conditions) {
            this.conditions = conditions;
        }

        @Override
        public int nextOffsetIndex(final IFeature feature, final HashMap<IFeature, Integer> features) {
            Integer featureValue = features.get(feature);
            if (featureValue == null) {
                return 0;
            }
            return conditions.get(featureValue);
        }
    }

    private static class Numeric implements ICondition<HashMap<IFeature, Integer>> {

        private final int cutPoint;

        public Numeric(int cutPoint) {
            this.cutPoint = cutPoint;
        }

        @Override
        public int nextOffsetIndex(final IFeature feature, final HashMap<IFeature, Integer> features) {
            Integer value = features.get(feature);
            if (value == null) {
                return 0;
            } else if (value > cutPoint) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}