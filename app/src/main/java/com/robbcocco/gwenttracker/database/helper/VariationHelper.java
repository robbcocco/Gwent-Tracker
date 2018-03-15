package com.robbcocco.gwenttracker.database.helper;


import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.dao.CardDao;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 3/6/2018.
 */

public class VariationHelper {
    private CardDao cardDao;

    public VariationHelper(CardDatabase cardDatabase) {
        cardDao = cardDatabase.cardDao();
    }

    public List<VariationModel> findVariationsByCardId(int cardId) {
        List<VariationModel> variationModelList = cardDao.findVariationsByCardId(cardId);

        for (VariationModel variationModel : variationModelList) {
            variationModel.setRarityModel(cardDao.findRarityByVarId(variationModel.getRarity_id()));
            variationModel.setSetModel(cardDao.findSetByVarId(variationModel.getSet_id()));
        }

        return variationModelList;
    }
}