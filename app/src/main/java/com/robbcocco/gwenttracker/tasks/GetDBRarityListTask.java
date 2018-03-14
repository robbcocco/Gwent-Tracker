package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.RarityModel;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBRarityListTask extends AsyncTask<Context, Void, List<RarityModel>> {

    private GetDBListInterface GetDBListInterface;

    public GetDBRarityListTask(GetDBListInterface GetDBListInterface) {
        this.GetDBListInterface = GetDBListInterface;
    }

    @Override
    protected List<RarityModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);

        return mDb.rarityDao().loadAllRarities();
    }

    @Override
    protected void onPostExecute(List<RarityModel> result) {
        GetDBListInterface.updateAdapter(result);
    }
}