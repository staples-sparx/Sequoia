package com.staples_sparx.sequoia;

import java.util.Set;

/**
 * Created by timbrooks on 4/28/15.
 */
public interface Forest<F, C> {
    double[] scoreTrees(C features);

    Forest<F, C> reduceToForest(C features, Set<F> missingFeatures);

    int[] getRoots();

    Node<F, C>[] getNodes();
}
