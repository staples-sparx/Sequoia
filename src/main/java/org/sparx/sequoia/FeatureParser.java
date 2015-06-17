package org.sparx.sequoia;

import java.io.BufferedReader;

/**
 * Created by timbrooks on 6/16/15.
 */
public interface FeatureParser<F> {
    F[] parseFeatures(BufferedReader reader);
}
