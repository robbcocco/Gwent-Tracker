package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBFactionListTask extends AsyncTask<Context, Void, List<FactionModel>> {

    private GetDBListInterface GetDBListInterface;

    public GetDBFactionListTask(GetDBListInterface GetDBListInterface) {
        this.GetDBListInterface = GetDBListInterface;
    }

    @Override
    protected List<FactionModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);

        return mDb.factionDao().loadAllFactions();
    }

    @Override
    protected void onPostExecute(List<FactionModel> result) {
        GetDBListInterface.updateAdapter(result);
    }
}