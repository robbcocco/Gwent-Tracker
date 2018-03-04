package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "card_loyalties",
        primaryKeys = {"card_id", "loyalty_id"},
        foreignKeys = {
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "card_id"),
                @ForeignKey(entity = LoyaltyModel.class,
                        parentColumns = "loyaltyid",
                        childColumns = "loyalty_id")})
public class CardLoyaltyModel {
    private int card_id;
    private int loyalty_id;

    public CardLoyaltyModel(int card_id, int loyalty_id) {
        this.card_id = card_id;
        this.loyalty_id = loyalty_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getLoyalty_id() {
        return loyalty_id;
    }

    public void setLoyalty_id(int loyalty_id) {
        this.loyalty_id = loyalty_id;
    }
}
