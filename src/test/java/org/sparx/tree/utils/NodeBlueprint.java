package org.sparx.tree.utils;

import org.sparx.tree.Condition;

import java.util.Map;

/**
 * Created by timbrooks on 5/15/14.
 */
public class NodeBlueprint {

    private final TestFeature feature;
    private final Condition<TestFeature, Map<TestFeature, Integer>> condition;

    public NodeBlueprint(TestFeature feature, Condition<TestFeature, Map<TestFeature, Integer>> condition) {
        this.feature = feature;
        this.condition = condition;
    }

    public TestFeature getFeature() {
        return feature;
    }

    public Condition<TestFeature, Map<TestFeature, Integer>> getCondition() {
        return condition;
    }
}
