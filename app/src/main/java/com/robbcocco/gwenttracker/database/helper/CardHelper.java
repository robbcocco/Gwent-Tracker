package com.robbcocco.gwenttracker.database.helper;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.dao.CardDao;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by rober on 3/6/2018.
 */

public class CardHelper {

    private CardDao cardDao;

//    @Inject
    public CardHelper(CardDatabase cardDatabase) {
        cardDao = cardDatabase.cardDao();
    }

    public LiveData<CardModel> findCardById(int id) {
        LiveData<CardModel> cardById = cardDao.findCardById(id);
        cardById = Transformations.switchMap(cardById, new Function<CardModel, LiveData<CardModel>>() {
            @Override
            public LiveData<CardModel> apply(final CardModel cardModel) {
                LiveData<List<VariationModel>> variationModelList = cardDao.findVariationByCardId(cardModel.id);
                LiveData<CardModel> cardModelLiveData = Transformations.map(variationModelList, new Function<List<VariationModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<VariationModel> input) {
                        cardModel.setVariationModelList(input);
                        return cardModel;
                    }
                });
                return cardModelLiveData;
            }
        });
        return cardById;
    }

    public LiveData<List<CardModel>> loadAllCards() {
        LiveData<List<CardModel>> allCards = cardDao.loadAllCards();
        allCards = Transformations.switchMap(allCards, new Function<List<CardModel>, LiveData<List<CardModel>>>() {
            @Override
            public LiveData<List<CardModel>> apply(final List<CardModel> input) {
                final MediatorLiveData<List<CardModel>> cardsMediatorLiveData = new MediatorLiveData<>();
                for (final CardModel cardModel : input) {
                    // Set faction
                    cardsMediatorLiveData.addSource(cardDao.findFactionById(cardModel.getFaction_id()), new Observer<FactionModel>() {
                        @Override
                        public void onChanged(@Nullable FactionModel model) {
                            cardModel.setFactionModel(model);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                    // Set variations
                    cardsMediatorLiveData.addSource(cardDao.findVariationByCardId(cardModel.id), new Observer<List<VariationModel>>() {
                        @Override
                        public void onChanged(@Nullable List<VariationModel> modelList) {
                            cardModel.setVariationModelList(modelList);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                    // Set categories
                    cardsMediatorLiveData.addSource(cardDao.getCategoriesByCardId(cardModel.id), new Observer<List<CategoryModel>>() {
                        @Override
                        public void onChanged(@Nullable List<CategoryModel> modelList) {
                            cardModel.setCategoryModelList(modelList);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                }
                return cardsMediatorLiveData;
            }
        });
        return allCards;
    }
//    public State getState(int id) {
//        State state = cardDao.getState(id);
//        state.setCities(cardDao.getCities(id));
//        return state;
//    }
//
//    public List<State> getStates() {
//        List<State> states = cardDao.getStates();
//        for (State state : states) {
//            state.setCities(cardDao.getCities(state.getId()));
//        }
//        return states;
//    }
}