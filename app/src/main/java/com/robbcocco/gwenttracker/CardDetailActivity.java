package com.robbcocco.gwenttracker;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CardDetailActivity extends AppCompatActivity {

    private static final String CARD_ID = "com.robbcocco.gwenttracker.card_id";

    public static Intent newIntent(Context context, int id) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra(CARD_ID, id);
        return intent;
    }

    protected Fragment createFragment() {
        int cardId = (int) getIntent().getSerializableExtra(CARD_ID);
        return CardDetailFragment.newInstance(cardId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.card_detail_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.card_detail_container, fragment).commit();
        }
    }
}
