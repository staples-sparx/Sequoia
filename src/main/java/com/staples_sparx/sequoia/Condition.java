package com.staples_sparx.sequoia;

/**
 * Created by timbrooks on 5/14/14.
 */
public interface Condition<F, C> {
    int nextOffsetIndex(final F feature, final C features);
}
