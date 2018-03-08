package com.robbcocco.gwenttracker;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.helper.CardHelper;

/**
 * Created by rober on 3/3/2018.
 */

public class CardDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application application;
    private int cardId;

    public CardDetailViewModelFactory(Application application, int cardId) {
        this.application = application;
        this.cardId = cardId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new CardDetailViewModel(application, cardId);
    }


    public class CardDetailViewModel extends AndroidViewModel {
        private CardDatabase mDb;
        private CardHelper cardHelper;
        private final LiveData<CardModel> cardModel;

        public CardDetailViewModel(Application application, int cardId) {
            super(application);

            mDb = CardDatabase.getDatabase(this.getApplication());
            cardHelper = new CardHelper(mDb);

            cardModel = cardHelper.findCardById(cardId);
        }

        public LiveData<CardModel> getCardModel() {
            return cardModel;
        }
    }
}
