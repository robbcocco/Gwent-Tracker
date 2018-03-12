package com.robbcocco.gwenttracker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.CardGenerator;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        new PopulateDatabaseAsyncTask(this).execute(getApplicationContext());

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    private static class PopulateDatabaseAsyncTask extends AsyncTask<Context, Void, Void> {
        private SplashActivity splashActivity;

        PopulateDatabaseAsyncTask(SplashActivity splashActivity) {
            this.splashActivity = splashActivity;
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
            Intent intent = new Intent(splashActivity, MainActivity.class);
            splashActivity.startActivity(intent);
            splashActivity.finish();
        }
    }
}
