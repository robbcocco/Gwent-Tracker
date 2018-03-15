package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;

import java.util.List;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDBCategoryListTask extends AsyncTask<Context, Void, List<CategoryModel>> {

    private GetDBListCallback GetDBListCallback;

    public GetDBCategoryListTask(GetDBListCallback GetDBListCallback) {
        this.GetDBListCallback = GetDBListCallback;
    }

    @Override
    protected List<CategoryModel> doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);

        return mDb.categoryDao().loadAllCategories();
    }

    @Override
    protected void onPostExecute(List<CategoryModel> result) {
        GetDBListCallback.updateAdapter(result);
    }
}