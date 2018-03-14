package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBCategoryListTask extends AsyncTask<Context, Void, List<CategoryModel>> {

    private GetDBListInterface GetDBListInterface;

    public GetDBCategoryListTask(GetDBListInterface GetDBListInterface) {
        this.GetDBListInterface = GetDBListInterface;
    }

    @Override
    protected List<CategoryModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);

        return mDb.categoryDao().loadAllCategories();
    }

    @Override
    protected void onPostExecute(List<CategoryModel> result) {
        GetDBListInterface.updateAdapter(result);
    }
}