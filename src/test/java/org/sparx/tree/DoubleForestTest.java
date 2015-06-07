package org.sparx.tree;

import org.junit.Before;
import org.sparx.tree.utils.TestFeature;
import org.sparx.tree.utils.TestTrees;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by timbrooks on 6/7/15.
 */
public class DoubleForestTest {

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
}
