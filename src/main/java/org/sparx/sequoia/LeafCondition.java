package org.sparx.sequoia;

/**
 * Created by timbrooks on 5/4/14.
 */
public class LeafCondition implements Condition<Integer, double[]> {

    @Override
    public int nextOffsetIndex(Integer feature, double[] features) {
        return 0;
    }
}
