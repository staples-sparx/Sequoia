package org.sparx.tree.utils;

import org.sparx.tree.ICondition;
import org.sparx.tree.Node;
import org.sparx.tree.Tree;

import java.util.*;

/**
 * Created by timbrooks on 5/15/14.
 */
public class TestTrees {

    private static final HashMap<Integer, NodeBlueprint> branchNodeRecipes;

    static {
        Map<Integer, Integer> categoricalMap = new HashMap<>();
        categoricalMap.put(0, 1);
        categoricalMap.put(1, 2);
        categoricalMap.put(2, 1);
        categoricalMap.put(3, 2);
        categoricalMap.put(4, 1);

        branchNodeRecipes = new HashMap<>();
        branchNodeRecipes.put(0, new NodeBlueprint(TestFeature.COST, new Numeric(5)));
        branchNodeRecipes.put(1, new NodeBlueprint(TestFeature.MONTH, new Categorical(categoricalMap)));
        branchNodeRecipes.put(2, new NodeBlueprint(TestFeature.DAY_OF_WEEK, new Categorical(categoricalMap)));
        branchNodeRecipes.put(3, new NodeBlueprint(TestFeature.COG, new Numeric(5)));
        branchNodeRecipes.put(4, new NodeBlueprint(TestFeature.DISTANCE, new Numeric(5)));
        branchNodeRecipes.put(5, new NodeBlueprint(TestFeature.CLASS_ID, new Categorical(categoricalMap)));
    }

    public Tree<TestFeature, Map<TestFeature, Integer>> getRandomTree() {
        Random random = new Random();

        List<Node<TestFeature, Map<TestFeature, Integer>>> nodes = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            NodeBlueprint nodeBlueprint = branchNodeRecipes.get(random.nextInt(6));
            int[] childOffsets = {random.nextInt(3) + 1, random.nextInt(3) + 1, random.nextInt(3) + 1};
            Node<TestFeature, Map<TestFeature, Integer>> node = new Node<>(nodeBlueprint.getFeature(), -1.0, false,
                    childOffsets, nodeBlueprint.getCondition());
            nodes.add(node);
        }

        for (int i = 1; i < 4; i++) {
            NodeBlueprint nodeBlueprint = branchNodeRecipes.get(random.nextInt(6));
            int[] childOffsets = {random.nextInt(8) + 4, random.nextInt(8) + 4, random.nextInt(8) + 4};
            Node<TestFeature, Map<TestFeature, Integer>> node = new Node<>(nodeBlueprint.getFeature(), -1.0, false,
                    childOffsets, nodeBlueprint.getCondition());
            nodes.add(node);
        }
        for (int i = 0; i < 8; i++) {
            int[] childOffsets = {};
            nodes.add(new Node<TestFeature, Map<TestFeature, Integer>>(null, random.nextInt(100), true, childOffsets, null));
        }
        return new Tree<>(nodes);
    }

    public Map<TestFeature, Integer> getRandomFeatures() {
        Random random = new Random();
        Map<TestFeature, Integer> featureMap = new HashMap<>();

        TestFeature[] numericFeatures = {TestFeature.COST, TestFeature.COG, TestFeature.DISTANCE};
        for (TestFeature numericFeature : numericFeatures) {
            if (random.nextInt(10) > 1) {
                featureMap.put(numericFeature, random.nextInt(10));
            }
        }

        TestFeature[] categoricalFeatures = {TestFeature.DAY_OF_WEEK, TestFeature.MONTH, TestFeature.CLASS_ID};
        for (TestFeature categoricalFeature : categoricalFeatures) {
            if (random.nextInt(10) > 1) {
                featureMap.put(categoricalFeature, random.nextInt(5));
            }
        }
        return featureMap;
    }

    private static class Categorical implements ICondition<TestFeature, Map<TestFeature, Integer>> {

        private final Map<Integer, Integer> conditions;

        public Categorical(Map<Integer, Integer> conditions) {
            this.conditions = conditions;
        }

        @Override
        public int nextOffsetIndex(final TestFeature feature, final Map<TestFeature, Integer> features) {
            Integer featureValue = features.get(feature);
            if (featureValue == null) {
                return 0;
            }
            return conditions.get(featureValue);
        }
    }

    private static class Numeric implements ICondition<TestFeature, Map<TestFeature, Integer>> {

        private final int cutPoint;

        public Numeric(int cutPoint) {
            this.cutPoint = cutPoint;
        }

        @Override
        public int nextOffsetIndex(final TestFeature feature, final Map<TestFeature, Integer> features) {
            Integer value = features.get(feature);
            if (value == null) {
                return 0;
            } else if (value > cutPoint) {
                return 1;
            } else {
                return 2;
            }
        }
    }
}
