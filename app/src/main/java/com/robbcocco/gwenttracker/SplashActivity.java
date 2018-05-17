package com.robbcocco.gwenttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.robbcocco.gwenttracker.database.CardDatabase;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        Toast.makeText(SplashActivity.this, getString(R.string.generating_collection), Toast.LENGTH_LONG).show();

        // Cheap way to populate the db before querying for the cards on first run
        CardDatabase.DB_THREAD.execute(new Runnable() {
            @Override
            public void run() {
                CardDatabase.getDatabase(getApplicationContext()).cardDao().testDB();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
