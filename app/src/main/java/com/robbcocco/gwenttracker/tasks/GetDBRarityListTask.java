package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.RarityModel;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBRarityListTask extends AsyncTask<Context, Void, List<RarityModel>> {

    private GetDBListCallback GetDBListCallback;

    public GetDBRarityListTask(GetDBListCallback GetDBListCallback) {
        this.GetDBListCallback = GetDBListCallback;
    }

    @Override
    protected List<RarityModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);

        return mDb.rarityDao().loadAllRarities();
    }

    @Override
    protected void onPostExecute(List<RarityModel> result) {
        GetDBListCallback.updateAdapter(result);
    }
}