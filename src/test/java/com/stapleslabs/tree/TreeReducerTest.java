package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;
import com.stapleslabs.utils.TestFeature;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Created by timbrooks on 6/11/14.
 */
public class TreeReducerTest {

    @Test
    public void testGetFastPath() {
        int[] node1Offsets = {1, 2};
        int[] node3Offsets = {3, 4};
        int[] node4Offsets = {4, 5};
        Node<Feature, Map<Feature, Integer>> node0 = new Node<>(Feature.COST, -1.0, false, node1Offsets, new DummyCondition());
        Node<Feature, Map<Feature, Integer>> node1 = new Node<>(null, 25.0, true, null, null);
        Node<Feature, Map<Feature, Integer>> node2 = new Node<>(Feature.MISSING, -1.0, false, node3Offsets, new DummyCondition());
        Node<Feature, Map<Feature, Integer>> node3 = new Node<>(Feature.COST, -1.0, false, node4Offsets, new DummyCondition());
        Node<Feature, Map<Feature, Integer>> node4 = new Node<>(null, 35.0, true, null, null);
        Node<Feature, Map<Feature, Integer>> node5 = new Node<>(null, 15.0, true, null, null);


        Tree<Feature, Map<Feature, Integer>> tree = new Tree<>(Arrays.asList(node0, node1, node2, node3, node4, node5));
        TreeReducer treeReducer = new TreeReducer();

        HashMap<Feature, Integer> features = new HashMap<>();
        features.put(Feature.COST, 10);

        HashSet<Feature> missingFeatures = new HashSet<>();
        missingFeatures.add(Feature.MISSING);

        Path path = new Path();
        treeReducer.getFastPath(0, tree.getNodes(), features, missingFeatures, path);

        int[][] expectedFastPath = {null, null, {2, 0}, null, null, null};
        assertArrayEquals(expectedFastPath, path.fastPath);
        assertEquals(2, path.root);
    }

    private enum Feature {
        COST,
        MISSING
    }

    private class DummyCondition implements ICondition<Feature, Map<Feature, Integer>> {

        @Override
        public int nextOffsetIndex(final Feature feature, final Map<Feature, Integer> features) {
            if (features.get(feature) > 5) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
