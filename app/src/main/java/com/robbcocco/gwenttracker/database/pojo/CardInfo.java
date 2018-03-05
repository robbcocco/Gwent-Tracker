package com.robbcocco.gwenttracker.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.robbcocco.gwenttracker.database.entity.CardModel;
import com.robbcocco.gwenttracker.database.entity.FactionModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 3/3/2018.
 */

public class CardInfo {
    @Embedded
    private CardModel cardModel;
    @Embedded
    private FactionModel factionModel;
    @Relation(parentColumn = "cardid", entityColumn = "card_id")
    private List<VariationModel> variationModelList;
//    @Relation(parentColumn = "category_id", entityColumn = "categoryid")
//    private List<CategoryModel> categoryModelList;

    public CardInfo() { }

    public CardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }

    public List<VariationModel> getVariationModelList() {
        return variationModelList;
    }

    public void setVariationModelList(List<VariationModel> variationModelList) {
        this.variationModelList = variationModelList;
    }

    public FactionModel getFactionModel() {
        return factionModel;
    }

    public void setFactionModel(FactionModel factionModel) {
        this.factionModel = factionModel;
    }
}
