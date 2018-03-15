package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

/**
 * Created by rober on 3/14/2018.
 */

public class GetCardDetailTask extends AsyncTask<Context, Void, CardModel> {
    private GetCardDetailCallback getCardDetailCallback;
    private final int cardId;

    public GetCardDetailTask(GetCardDetailCallback getCardDetailCallback, int cardId) {
        this.getCardDetailCallback = getCardDetailCallback;
        this.cardId = cardId;
    }

    @Override
    protected CardModel doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        CardHelper cardHelper = new CardHelper(mDb);

        return cardHelper.findCardById(cardId);
    }

    @Override
    protected void onPostExecute(CardModel result) {
        getCardDetailCallback.updateAdapter(result);
    }
}