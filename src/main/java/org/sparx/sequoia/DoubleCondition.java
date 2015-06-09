package org.sparx.sequoia;

/**
 * Created by timbrooks on 6/7/15.
 */
public interface DoubleCondition<C> {
    int childOffset(double cutPoint, int feature, C features);
}
