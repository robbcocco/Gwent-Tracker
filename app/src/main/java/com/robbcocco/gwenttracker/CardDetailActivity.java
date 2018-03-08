package com.robbcocco.gwenttracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Window;

public class CardDetailActivity extends AppCompatActivity {

    private static final String CARD_ID = "com.robbcocco.gwenttracker.card_id";

    public static Intent newIntent(Context context, Integer cardId) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra(CARD_ID, cardId);
        return intent;
    }

    protected Fragment createFragment() {
        Integer cardId = (Integer) getIntent().getSerializableExtra(CARD_ID);
        return CardDetailFragment.newInstance(cardId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Explode());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.card_detail_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.card_detail_container, fragment).commit();
        }
    }
}
