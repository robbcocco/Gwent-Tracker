package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "related_cards",
        primaryKeys = {"card_id", "related_card_id"},
        foreignKeys = {
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "card_id"),
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "related_card_id")})
public class RelatedCardModel {
    private int card_id;
    private int related_card_id;

    public RelatedCardModel(int card_id, int related_card_id) {
        this.card_id = card_id;
        this.related_card_id = related_card_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getRelated_card_id() {
        return related_card_id;
    }

    public void setRelated_card_id(int related_card_id) {
        this.related_card_id = related_card_id;
    }
}
