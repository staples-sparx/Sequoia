package com.staples_sparx.sequoia.scikit;

import com.staples_sparx.sequoia.Condition;

/**
 * Created by timbrooks on 7/10/14.
 */
public class NumericCondition implements Condition<Integer, double[]> {

    private final double cutPoint;

    public NumericCondition(double cutPoint) {
        this.cutPoint = cutPoint;
    }

    @Override
    public int nextOffsetIndex(Integer feature, double[] features) {
        double value = features[feature];
        if (value <= cutPoint) {
            return 0;
        } else {
            return 1;
        }
    }
}
