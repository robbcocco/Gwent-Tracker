package com.robbcocco.gwenttracker.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

/**
 * Created by rober on 2/27/2018.
 */

@Entity(tableName = "card_keywords",
        primaryKeys = {"card_id", "keyword_id"},
        indices = {@Index("card_id"), @Index("keyword_id")},
        foreignKeys = {
                @ForeignKey(entity = CardModel.class,
                        parentColumns = "cardid",
                        childColumns = "card_id"),
                @ForeignKey(entity = KeywordModel.class,
                        parentColumns = "keywordid",
                        childColumns = "keyword_id")})
public class CardKeywordModel {
    private int card_id;
    private int keyword_id;

    public CardKeywordModel(int card_id, int keyword_id) {
        this.card_id = card_id;
        this.keyword_id = keyword_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getKeyword_id() {
        return keyword_id;
    }

    public void setKeyword_id(int keyword_id) {
        this.keyword_id = keyword_id;
    }
}
