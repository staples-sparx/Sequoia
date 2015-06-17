package com.staples_sparx.sequoia.scikit;

import com.staples_sparx.sequoia.Forest;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * Created by timbrooks on 6/16/15.
 */
public class ScikitGBM {

    public final Forest<Integer, double[]> forest;
    public final double learningRate;
    public final double initialValue;
    public final Int2ObjectMap<String> featureIndexMap;

    public ScikitGBM(Forest<Integer, double[]> forest, Int2ObjectMap<String> featureIndexMap, double learningRate,
                     double initialValue) {
        this.forest = forest;
        this.featureIndexMap = featureIndexMap;
        this.learningRate = learningRate;
        this.initialValue = initialValue;
    }
}
