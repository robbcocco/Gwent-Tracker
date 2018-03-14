package com.robbcocco.gwenttracker.tasks;

import com.robbcocco.gwenttracker.database.entity.CardModel;

/**
 * Created by rober on 3/14/2018.
 */

public interface GetCardDetailInterface {
    void updateAdapter(CardModel result);
}
