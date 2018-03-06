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
import com.robbcocco.gwenttracker.database.entity.KeywordModel;
import com.robbcocco.gwenttracker.database.entity.LoyaltyModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 3/6/2018.
 */

public class CardHelper {
    private CardDao cardDao;
    private VariationHelper variationHelper;

    public CardHelper(CardDatabase cardDatabase) {
        cardDao = cardDatabase.cardDao();
        variationHelper = new VariationHelper(cardDatabase);
    }

    public LiveData<CardModel> findCardById(int id) {
        LiveData<CardModel> cardById = cardDao.findCardById(id);
        cardById = Transformations.switchMap(cardById, new Function<CardModel, LiveData<CardModel>>() {
            @Override
            public LiveData<CardModel> apply(final CardModel cardModel) {
                LiveData<CardModel> cardModelLiveData;
                // Set faction
                cardModelLiveData = Transformations.map(cardDao.findFactionById(cardModel.getFaction_id()), new Function<FactionModel, CardModel>() {
                    @Override
                    public CardModel apply(FactionModel input) {
                        cardModel.setFactionModel(input);
                        return cardModel;
                    }
                });
                // Set variations
                cardModelLiveData = Transformations.map(variationHelper.findVariationsByCardId(cardModel.id), new Function<List<VariationModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<VariationModel> input) {
                        cardModel.setVariationModelList(input);
                        return cardModel;
                    }
                });
                // Set categories
                cardModelLiveData = Transformations.map(cardDao.getCategoriesByCardId(cardModel.id), new Function<List<CategoryModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<CategoryModel> input) {
                        cardModel.setCategoryModelList(input);
                        return cardModel;
                    }
                });
                // Set keywords
                cardModelLiveData = Transformations.map(cardDao.getKeywordsByCardId(cardModel.id), new Function<List<KeywordModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<KeywordModel> input) {
                        cardModel.setKeywordModelList(input);
                        return cardModel;
                    }
                });
                // Set loyalties
                cardModelLiveData = Transformations.map(cardDao.getLoyaltiesByCardId(cardModel.id), new Function<List<LoyaltyModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<LoyaltyModel> input) {
                        cardModel.setLoyaltyModelList(input);
                        return cardModel;
                    }
                });
                // Set related cards
                cardModelLiveData = Transformations.map(cardDao.getRelatedCards(cardModel.id), new Function<List<CardModel>, CardModel>() {
                    @Override
                    public CardModel apply(List<CardModel> input) {
                        cardModel.setRelatedCardModelList(input);
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
                    cardsMediatorLiveData.addSource(variationHelper.findVariationsByCardId(cardModel.id), new Observer<List<VariationModel>>() {
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
                    // Set keywords
                    cardsMediatorLiveData.addSource(cardDao.getKeywordsByCardId(cardModel.id), new Observer<List<KeywordModel>>() {
                        @Override
                        public void onChanged(@Nullable List<KeywordModel> modelList) {
                            cardModel.setKeywordModelList(modelList);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                    // Set loyalties
                    cardsMediatorLiveData.addSource(cardDao.getLoyaltiesByCardId(cardModel.id), new Observer<List<LoyaltyModel>>() {
                        @Override
                        public void onChanged(@Nullable List<LoyaltyModel> modelList) {
                            cardModel.setLoyaltyModelList(modelList);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                    // Set related cards
                    cardsMediatorLiveData.addSource(cardDao.getRelatedCards(cardModel.id), new Observer<List<CardModel>>() {
                        @Override
                        public void onChanged(@Nullable List<CardModel> modelList) {
                            cardModel.setRelatedCardModelList(modelList);
                            cardsMediatorLiveData.postValue(input);
                        }
                    });
                }
                return cardsMediatorLiveData;
            }
        });
        return allCards;
    }
}