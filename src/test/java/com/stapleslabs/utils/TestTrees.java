package com.stapleslabs.utils;

import com.stapleslabs.features.IFeature;
import com.stapleslabs.tree.ICondition;
import com.stapleslabs.tree.Node;
import com.stapleslabs.tree.Tree;

import java.util.*;

/**
 * Created by timbrooks on 5/15/14.
 */
public class TestTrees {

    private static final HashMap<Integer, NodeBlueprint> branchNodeRecipes;

    static {
        HashMap<Integer, Integer> categoricalMap = new HashMap<>();
        categoricalMap.put(0, 1);
        categoricalMap.put(1, 2);
        categoricalMap.put(2, 1);
        categoricalMap.put(3, 2);
        categoricalMap.put(4, 1);

        branchNodeRecipes = new HashMap<>();
        branchNodeRecipes.put(0, new NodeBlueprint(Feature.COST, new Numeric(5)));
        branchNodeRecipes.put(1, new NodeBlueprint(Feature.MONTH, new Categorical(categoricalMap)));
        branchNodeRecipes.put(2, new NodeBlueprint(Feature.DAY_OF_WEEK, new Categorical(categoricalMap)));
        branchNodeRecipes.put(3, new NodeBlueprint(Feature.COG, new Numeric(5)));
        branchNodeRecipes.put(4, new NodeBlueprint(Feature.DISTANCE, new Numeric(5)));
        branchNodeRecipes.put(5, new NodeBlueprint(Feature.CLASS_ID, new Categorical(categoricalMap)));
    }

    public Tree<Map<IFeature, Integer>> getRandomTree() {
        Random random = new Random();

        List<Node<Map<IFeature, Integer>>> nodes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            NodeBlueprint nodeBlueprint = branchNodeRecipes.get(random.nextInt(6));
            int[] childOffsets = {random.nextInt(8) + 4, random.nextInt(8) + 4, random.nextInt(8) + 4};
            Node<Map<IFeature, Integer>> node = new Node<>(nodeBlueprint.getFeature(), -1.0, false, childOffsets, nodeBlueprint.getCondition());
            nodes.add(node);
        }
        for (int i = 0; i < 8; i++) {
            int[] childOffsets = {};
            nodes.add(new Node<Map<IFeature, Integer>>(null, random.nextInt(100), true, childOffsets, null));
        }
        return new Tree<>(nodes);
    }

    public Map<IFeature, Integer> getRandomFeatures() {
        Random random = new Random();
        HashMap<IFeature, Integer> featureMap = new HashMap<>();

        IFeature[] numericFeatures = {Feature.COST, Feature.COG, Feature.DISTANCE};
        for (IFeature numericFeature : numericFeatures) {
            if (random.nextInt(10) > 1) {
                featureMap.put(numericFeature, random.nextInt(10));
            }
        }

        IFeature[] categoricalFeatures = {Feature.DAY_OF_WEEK, Feature.MONTH, Feature.CLASS_ID};
        for (IFeature categoricalFeature : categoricalFeatures) {
            if (random.nextInt(10) > 1) {
                featureMap.put(categoricalFeature, random.nextInt(5));
            }
        }
        return featureMap;
    }

    public enum Feature implements IFeature {
        COST,
        MONTH,
        DAY_OF_WEEK,
        COG,
        DISTANCE,
        CLASS_ID
    }

    private static class Categorical implements ICondition<Map<IFeature, Integer>> {

        private final Map<Integer, Integer> conditions;

        public Categorical(Map<Integer, Integer> conditions) {
            this.conditions = conditions;
        }

        @Override
        public int nextOffsetIndex(final IFeature feature, final Map<IFeature, Integer> features) {
            Integer featureValue = features.get(feature);
            if (featureValue == null) {
                return 0;
            }
            return conditions.get(featureValue);
        }
    }

    private static class Numeric implements ICondition<Map<IFeature, Integer>> {

        private final int cutPoint;

        public Numeric(int cutPoint) {
            this.cutPoint = cutPoint;
        }

        @Override
        public int nextOffsetIndex(final IFeature feature, final Map<IFeature, Integer> features) {
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
