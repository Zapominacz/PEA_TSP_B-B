package algoritms;

import models.TspMap;

/**
 * Created by Quantum on 2015-10-12.
 */
public interface Algorithm {

    int INF = Integer.MAX_VALUE;
    int NO_ITEM = -1;

    TspMap performAlgorithm(final TspMap map);
    TspMap getResult();
}
