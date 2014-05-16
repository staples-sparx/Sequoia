package com.stapleslabs.utils;

import com.stapleslabs.features.IFeature;
import com.stapleslabs.tree.ICondition;

import java.util.Map;

/**
 * Created by timbrooks on 5/15/14.
 */
public class NodeBlueprint {

    private final IFeature feature;
    private final ICondition<Map<IFeature, Integer>> condition;

    public NodeBlueprint(IFeature feature, ICondition<Map<IFeature, Integer>> condition) {
        this.feature = feature;
        this.condition = condition;
    }

    public IFeature getFeature() {
        return feature;
    }

    public ICondition<Map<IFeature, Integer>> getCondition() {
        return condition;
    }
}
