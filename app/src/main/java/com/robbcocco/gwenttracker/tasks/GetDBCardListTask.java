package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBCardListTask extends AsyncTask<Context, Void, List<CardModel>> {

    private GetDBListInterface GetDBListInterface;

    public GetDBCardListTask(GetDBListInterface GetDBListInterface) {
        this.GetDBListInterface = GetDBListInterface;
    }

    @Override
    protected List<CardModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        CardHelper cardHelper = new CardHelper(mDb);

        return cardHelper.loadAllCards();
    }

    @Override
    protected void onPostExecute(List<CardModel> result) {
        GetDBListInterface.updateAdapter(result);
    }
}