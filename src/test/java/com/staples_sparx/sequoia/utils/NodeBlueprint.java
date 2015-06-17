package com.staples_sparx.sequoia.utils;

import com.staples_sparx.sequoia.Condition;

/**
 * Created by timbrooks on 5/15/14.
 */
public class NodeBlueprint {

    private final Integer feature;
    private final Condition<Integer, double[]> condition;

    public NodeBlueprint(Integer feature, Condition<Integer, double[]> condition) {
        this.feature = feature;
        this.condition = condition;
    }

    public Integer getFeature() {
        return feature;
    }

    public Condition<Integer, double[]> getCondition() {
        return condition;
    }
}
