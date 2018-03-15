package com.robbcocco.gwenttracker.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.CardGenerator;

/**
 * Created by rober on 3/14/2018.
 */

public class GetDatabaseTask extends AsyncTask<Context, Void, Void> {
    private GetDatabaseCallback getDatabaseCallback;

    public GetDatabaseTask(GetDatabaseCallback getDatabaseCallback) {
        this.getDatabaseCallback = getDatabaseCallback;
    }

    @Override
    protected Void doInBackground(Context... context) {
        CardDatabase mDb = CardDatabase.getDatabase(context[0]);
        if (mDb.cardDao().testDB().isEmpty()) {
            CardGenerator.GenerateCollection(mDb, context[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        getDatabaseCallback.startActivity();
    }
}
