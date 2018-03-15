package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by rober on 3/14/2018.
 */

public class GetCardListTask extends AsyncTask<Context, Void, List<CardModel>> {

    private GetCardListCallback getCardListInterface;

    public GetCardListTask(GetCardListCallback getCardListInterface) {
        this.getCardListInterface = getCardListInterface;
    }

    @Override
    protected List<CardModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        List<CardModel> cardModelStubList = mDb.cardDao().loadAllCards();

        final List<CardModel> cardModelList = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (CardModel card : cardModelStubList) {
            if (isCancelled()) { break; }
            GetCardDetailCallback getCardDetailCallback = new GetCardDetailCallback() {
                @Override
                public void updateAdapter(CardModel result) {
                    cardModelList.add(result);
                }
            };
            new GetCardDetailTask(getCardDetailCallback, card.id)
                    .executeOnExecutor(executor, context[0]);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return cardModelList;
    }

    @Override
    protected void onPostExecute(List<CardModel> result) {
        getCardListInterface.updateAdapter(result);
    }
}