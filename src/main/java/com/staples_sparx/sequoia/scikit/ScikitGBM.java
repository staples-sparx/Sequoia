/*
 * Copyright 2014 Staples Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.staples_sparx.sequoia.scikit;

import com.staples_sparx.sequoia.Forest;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class ScikitGBM {

    public final Forest<Integer, double[]> forest;
    public final double learningRate;
    public final double initialValue;
    public final Int2ObjectMap<String> featureIndexMap;

    public ScikitGBM(Forest<Integer, double[]> forest, Int2ObjectMap<String> featureIndexMap, double initialValue,
                     double learningRate) {
        this.forest = forest;
        this.featureIndexMap = featureIndexMap;
        this.learningRate = learningRate;
        this.initialValue = initialValue;
    }
}
