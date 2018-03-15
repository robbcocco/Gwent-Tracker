package com.robbcocco.gwenttracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.robbcocco.gwenttracker.tasks.GetDatabaseTask;
import com.robbcocco.gwenttracker.tasks.GetDatabaseCallback;

public class SplashActivity extends AppCompatActivity {
    private GetDatabaseTask getDatabaseTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);

        GetDatabaseCallback getDatabaseCallback = new GetDatabaseCallback() {
            @Override
            public void startActivity() {
                startMainActivity();
            }
        };
        getDatabaseTask = new GetDatabaseTask(getDatabaseCallback);
        getDatabaseTask.execute(getApplicationContext());
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
