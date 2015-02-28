package com.stapleslabs.utils;

import com.stapleslabs.tree.ICondition;

import java.util.Map;

/**
 * Created by timbrooks on 5/15/14.
 */
public class NodeBlueprint {

    private final TestFeature feature;
    private final ICondition<TestFeature, Map<TestFeature, Integer>> condition;

    public NodeBlueprint(TestFeature feature, ICondition<TestFeature, Map<TestFeature, Integer>> condition) {
        this.feature = feature;
        this.condition = condition;
    }

    public TestFeature getFeature() {
        return feature;
    }

    public ICondition<TestFeature, Map<TestFeature, Integer>> getCondition() {
        return condition;
    }
}
