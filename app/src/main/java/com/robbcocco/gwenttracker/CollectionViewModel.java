package com.robbcocco.gwenttracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardInfoModel;

import java.util.List;

/**
 * Created by rober on 3/3/2018.
 */

public class CollectionViewModel extends AndroidViewModel {
    private CardDatabase mDb;
    private final LiveData<List<CardInfoModel>> cardModelList;

    public CollectionViewModel(Application application) {
        super(application);

        mDb = CardDatabase.getDatabase(this.getApplication());

        cardModelList = mDb.cardInfoDao().findAllCardInfo();
    }

    public LiveData<List<CardInfoModel>> getCardModelList() {
        return cardModelList;
    }
}
