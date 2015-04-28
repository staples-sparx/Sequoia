package com.stapleslabs.tree;

/**
 * Created by timbrooks on 4/28/15.
 */
public class SpecializedCondition {

    public int childOffset(double cutPoint, int feature, double[] features) {
        double value = features[feature];
        if (value <= cutPoint) {
            return 0;
        } else {
            return 1;
        }
    }
}
