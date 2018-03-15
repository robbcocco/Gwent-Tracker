package com.robbcocco.gwenttracker.tasks;

import com.robbcocco.gwenttracker.database.entity.CardModel;

/**
 * Created by rober on 3/14/2018.
 */

public interface GetCardDetailCallback {
    void updateAdapter(CardModel result);
}
