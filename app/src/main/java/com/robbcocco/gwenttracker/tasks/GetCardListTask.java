package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by rober on 3/14/2018.
 */

public class GetCardListTask extends AsyncTask<Context, Void, List<CardModel>> {

    private GetCardListCallback getCardListInterface;
    private ExecutorService executor;

    public GetCardListTask(GetCardListCallback getCardListInterface) {
        this.getCardListInterface = getCardListInterface;
    }

    @Override
    protected List<CardModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        List<CardModel> cardModelList = mDb.cardDao().loadAllCards();
        executor = Executors.newFixedThreadPool(10);
        for (CardModel card : cardModelList) {
            if (isCancelled()) { break; }
            new GetCardDetailTask(getCardListInterface, card.id)
                    .executeOnExecutor(executor, context[0]);
        }

        return cardModelList;
    }

    @Override
    protected void onPostExecute(List<CardModel> result) {
        getCardListInterface.updateAdapter(result);
    }
}