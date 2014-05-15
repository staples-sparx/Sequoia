package com.stapleslabs.tree;

import com.stapleslabs.features.IFeature;

/**
 * Created by timbrooks on 5/14/14.
 */
public interface ICondition<T> {
    public int nextOffsetIndex(final IFeature feature, final T features);
}
