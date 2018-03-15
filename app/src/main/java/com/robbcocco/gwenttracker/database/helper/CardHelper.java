package com.robbcocco.gwenttracker.database.helper;


import com.robbcocco.gwenttracker.database.CardDatabase;
import com.robbcocco.gwenttracker.database.dao.CardDao;
import com.robbcocco.gwenttracker.database.entity.CardModel;

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

    public CardModel findCardById(int id) {
        CardModel cardModel = cardDao.findCardById(id);

        updateCard(cardModel);

        return cardModel;
    }

    public List<CardModel> loadAllCards() {
        List<CardModel> cardModelList = cardDao.loadAllCards();

        for (CardModel cardModel : cardModelList) {
            updateCard(cardModel);
        }

        return cardModelList;
    }

    private CardModel updateCard(CardModel cardModel) {
        cardModel.setFactionModel(cardDao.findFactionById(cardModel.getFaction_id()));
        cardModel.setVariationModelList(variationHelper.findVariationsByCardId(cardModel.id));
        cardModel.setCategoryModelList(cardDao.getCategoriesByCardId(cardModel.id));
        cardModel.setKeywordModelList(cardDao.getKeywordsByCardId(cardModel.id));
        cardModel.setLoyaltyModelList(cardDao.getLoyaltiesByCardId(cardModel.id));
        cardModel.setRelatedCardModelList(cardDao.getRelatedCards(cardModel.id));
        return cardModel;
    }
}