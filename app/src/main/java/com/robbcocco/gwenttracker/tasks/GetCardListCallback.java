package com.robbcocco.gwenttracker.tasks;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public interface GetCardListCallback extends GetCardDetailCallback {
    void updateAdapter(List result);
}
