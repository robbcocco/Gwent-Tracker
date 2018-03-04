package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.Embedded;

/**
 * Created by rober on 3/3/2018.
 */

public class CardInfoModel {
    @Embedded
    private CardModel cardModel;

    @Embedded
    private VariationModel variationModel;

    public CardInfoModel() { }

    public CardModel getCardModel() {
        return cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }

    public VariationModel getVariationModel() {
        return variationModel;
    }

    public void setVariationModel(VariationModel variationModel) {
        this.variationModel = variationModel;
    }
}
