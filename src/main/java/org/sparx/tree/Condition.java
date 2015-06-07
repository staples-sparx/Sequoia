package org.sparx.tree;

/**
 * Created by timbrooks on 5/14/14.
 */
public interface Condition<F, C> {
    int nextOffsetIndex(final F feature, final C features);
}
