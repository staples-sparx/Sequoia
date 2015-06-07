package org.sparx.tree.utils;

import org.sparx.tree.Condition;
import org.sparx.tree.Node;
import org.sparx.tree.Tree;

import java.util.*;

/**
 * Created by timbrooks on 5/15/14.
 */
public class TestTrees {

    private static final HashMap<Integer, NodeBlueprint> branchNodeRecipes;

    static {
        Map<Double, Integer> categoricalMap = new HashMap<>();
        categoricalMap.put(0d, 1);
        categoricalMap.put(1d, 2);
        categoricalMap.put(2d, 1);
        categoricalMap.put(3d, 2);
        categoricalMap.put(4d, 1);

        branchNodeRecipes = new HashMap<>();
        branchNodeRecipes.put(0, new NodeBlueprint(0, new Numeric(5)));
        branchNodeRecipes.put(1, new NodeBlueprint(1, new Categorical(categoricalMap)));
        branchNodeRecipes.put(2, new NodeBlueprint(2, new Categorical(categoricalMap)));
        branchNodeRecipes.put(3, new NodeBlueprint(3, new Numeric(5)));
        branchNodeRecipes.put(4, new NodeBlueprint(4, new Numeric(5)));
        branchNodeRecipes.put(5, new NodeBlueprint(5, new Categorical(categoricalMap)));
    }

    public Tree<Integer, double[]> getRandomTree(boolean includeCategorical) {
        Random random = new Random();

        List<Node<Integer, double[]>> nodes = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            NodeBlueprint nodeBlueprint = branchNodeRecipes.get(random.nextInt(6));
            int[] childOffsets = {random.nextInt(3) + 1, random.nextInt(3) + 1, random.nextInt(3) + 1};
            Node<Integer, double[]> node = new Node<>(nodeBlueprint.getFeature(), -1.0, false,
                    childOffsets, nodeBlueprint.getCondition());
            nodes.add(node);
        }

        for (int i = 1; i < 4; i++) {
            NodeBlueprint nodeBlueprint = branchNodeRecipes.get(random.nextInt(6));
            int[] childOffsets = {random.nextInt(8) + 4, random.nextInt(8) + 4, random.nextInt(8) + 4};
            Node<Integer, double[]> node = new Node<>(nodeBlueprint.getFeature(), -1.0, false,
                    childOffsets, nodeBlueprint.getCondition());
            nodes.add(node);
        }
        for (int i = 0; i < 8; i++) {
            int[] childOffsets = {};
            nodes.add(new Node<Integer, double[]>(null, random.nextInt(100), true, childOffsets, null));
        }
        return new Tree<>(nodes);
    }

    public double[] getRandomFeatures() {
        Random random = new Random();
        double[] features = new double[6];

        int[] numericFeatures = {0, 3, 4};
        for (int numericFeature : numericFeatures) {
            if (random.nextInt(10) > 1) {
                features[numericFeature] = random.nextInt(10);
            } else {
                features[numericFeature] = -1.0;
            }
        }

        int[] categoricalFeatures = {2, 1, 5};
        for (int categoricalFeature : categoricalFeatures) {
            if (random.nextInt(10) > 1) {
                features[categoricalFeature] = random.nextInt(5);
            } else {
                features[categoricalFeature] = -1.0;
            }
        }
        return features;
    }

    private static class Categorical implements Condition<Integer, double[]> {

        private final Map<Double, Integer> conditions;

        public Categorical(Map<Double, Integer> conditions) {
            this.conditions = conditions;
        }

        @Override
        public int nextOffsetIndex(Integer feature, double[] features) {
            double featureValue = features[feature];
            if (featureValue == -1.0) {
                return 0;
            }
            return conditions.get(featureValue);
        }
    }

    private static class Numeric implements Condition<Integer, double[]> {

        private final int cutPoint;

        public Numeric(int cutPoint) {
            this.cutPoint = cutPoint;
        }

        @Override
        public int nextOffsetIndex(final Integer feature, double[] features) {
            double value = features[feature];
            if (value == -1.0) {
                return 0;
            } else if (value > cutPoint) {
                return 1;
            } else {
                return 2;
            }
        }
    }
}
