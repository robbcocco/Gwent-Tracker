package com.robbcocco.gwenttracker.database.helper;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.Nullable;

import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.dao.VariationDao;
import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.CategoryModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.entity.KeywordModel;
import com.robbcocco.gwenttracker.database.entity.LoyaltyModel;
import com.robbcocco.gwenttracker.database.entity.RarityModel;
import com.robbcocco.gwenttracker.database.entity.SetModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 3/6/2018.
 */

public class VariationHelper {
    private VariationDao variationDao;

    public VariationHelper(CardDatabase cardDatabase) {
        variationDao = cardDatabase.variationDao();
    }

    public LiveData<List<VariationModel>> findVariationsByCardId(int cardId) {
        LiveData<List<VariationModel>> variationsByCardId = variationDao.findVariationsByCardId(cardId);
        variationsByCardId = Transformations.switchMap(variationsByCardId, new Function<List<VariationModel>, LiveData<List<VariationModel>>>() {
            @Override
            public LiveData<List<VariationModel>> apply(final List<VariationModel> input) {
                final MediatorLiveData<List<VariationModel>> variationsMediatorLiveData = new MediatorLiveData<>();
                for (final VariationModel variationModel : input) {
                    // Set rarity
                    variationsMediatorLiveData.addSource(variationDao.findRarityById(variationModel.getRarity_id()), new Observer<RarityModel>() {
                        @Override
                        public void onChanged(@Nullable RarityModel model) {
                            variationModel.setRarityModel(model);
                            variationsMediatorLiveData.postValue(input);
                        }
                    });
                    // Set set
                    variationsMediatorLiveData.addSource(variationDao.findSetById(variationModel.getSet_id()), new Observer<SetModel>() {
                        @Override
                        public void onChanged(@Nullable SetModel model) {
                            variationModel.setSetModel(model);
                            variationsMediatorLiveData.postValue(input);
                        }
                    });
                }
                return variationsMediatorLiveData;
            }
        });
        return variationsByCardId;
    }
}