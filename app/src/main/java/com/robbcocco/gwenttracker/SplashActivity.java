package com.robbcocco.gwenttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.robbcocco.gwenttracker.tasks.GetDatabaseTask;
import com.robbcocco.gwenttracker.tasks.GetDatabaseInterface;

public class SplashActivity extends AppCompatActivity {
    private GetDatabaseTask getDatabaseTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        GetDatabaseInterface getDatabaseInterface = new GetDatabaseInterface() {
            @Override
            public void startActivity() {
                startMainActivity();
            }
        };
        getDatabaseTask = new GetDatabaseTask(getDatabaseInterface);
        getDatabaseTask.execute(getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        getDatabaseTask.cancel(true);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
