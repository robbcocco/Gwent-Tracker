package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.CardModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CardModel> cards);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CardModel... cards);

    @Query("SELECT * FROM cards")
    List<CardModel> testDB();

    @Query("SELECT * FROM cards")
    LiveData<List<CardModel>> loadAllCards();

    @Query("SELECT cardid FROM cards WHERE card_tag LIKE :tag LIMIT 1")
    int getIdByTag(String tag);

    @Query("SELECT * FROM cards WHERE cardid = :id LIMIT 1")
    LiveData<CardModel> findCardById(int id);

    @Query("SELECT * FROM cards WHERE card_name LIKE '%'||:name||'%'")
    LiveData<List<CardModel>> findCardsByName(String name);

    @Query("SELECT * FROM cards WHERE faction_id IN (:factionIds)")
    LiveData<List<CardModel>> findCardsByFilters(int[] factionIds);

    @Query("SELECT * FROM cards INNER JOIN related_cards ON cards.cardid = related_cards.related_card_id WHERE related_cards.card_id = :cardId")
    LiveData<List<CardModel>> getRelatedCards(int cardId);
}
