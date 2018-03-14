package com.robbcocco.gwenttracker.tasks;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public interface GetCardListInterface extends GetCardDetailInterface {
    void updateAdapter(List result);
}
