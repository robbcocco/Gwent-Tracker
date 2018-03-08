package com.robbcocco.gwenttracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.helper.CardHelper;
import com.robbcocco.gwenttracker.database.entity.CardModel;

import java.util.List;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionViewModel extends AndroidViewModel {
    private CollectionViewModel INSTANCE=null;

    private CardDatabase mDb;
    private CardHelper cardHelper;
    private final LiveData<List<CardModel>> cardModelList;

    public CollectionViewModel(Application application) {
        super(application);

        mDb = CardDatabase.getDatabase(this.getApplication());
        cardHelper = new CardHelper(mDb);

        cardModelList = cardHelper.loadAllCards();
        INSTANCE = this;
    }

    public LiveData<List<CardModel>> getCardModelList() {
        return cardModelList;
    }
}
