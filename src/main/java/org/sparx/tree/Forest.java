package org.sparx.tree;

import java.util.List;
import java.util.Set;

/**
 * Created by timbrooks on 4/28/15.
 */
public interface Forest<F, C> {
    double[] reduceToValues(C features);

    Forest<F, C> reduceToForest(C features, Set<F> missingFeatures);

    double[][] optimizedReduceToValues(List<C> features, Set<F> differingFeatures);

    int[] getRoots();

    Node<F, C>[] getNodes();
}
