package com.stapleslabs.tree;

/**
 * Created by timbrooks on 5/14/14.
 */
public interface ICondition<F, C> {
    public int nextOffsetIndex(final F feature, final C features);
}
