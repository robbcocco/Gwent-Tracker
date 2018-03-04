package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "card_categories",
        primaryKeys = {"card_id", "category_id"},
        foreignKeys = {
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "card_id"),
                @ForeignKey(entity = CategoryModel.class,
                        parentColumns = "categoryid",
                        childColumns = "category_id")})
public class CardCategoryModel {
    private int card_id;
    private int category_id;

    public CardCategoryModel(int card_id, int category_id) {
        this.card_id = card_id;
        this.category_id = category_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
}
