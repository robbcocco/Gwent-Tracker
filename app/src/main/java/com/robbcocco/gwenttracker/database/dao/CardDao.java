package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
 * Created by rober on 2/27/2018.
 */

@Dao
public interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CardModel> cards);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CardModel... cards);

    @Query("SELECT cardid FROM cards WHERE card_tag LIKE :tag LIMIT 1")
    int getIdByTag(String tag);

    @Query("SELECT * FROM cards")
    List<CardModel> testDB();

    @Query("SELECT * FROM cards WHERE cardid = :id LIMIT 1")
    CardModel findCardById(int id);

    @Query("SELECT * FROM factions WHERE factionid = :factionId LIMIT 1")
    FactionModel findFactionById(int factionId);

    @Query("SELECT DISTINCT cards.* FROM cards INNER JOIN variations ON cards.cardid = variations.card_id WHERE variations.collectible = 1")
    List<CardModel> loadAllCards();

    @Query("SELECT * FROM variations WHERE card_id = :cardId")
    List<VariationModel> findVariationsByCardId(int cardId);

    @Query("SELECT * FROM rarities WHERE rarityid = :rarityId LIMIT 1")
    RarityModel findRarityByVarId(int rarityId);

    @Query("SELECT * FROM sets WHERE setid = :setId LIMIT 1")
    SetModel findSetByVarId(int setId);

    @Query("SELECT * FROM categories INNER JOIN card_categories ON categories.categoryid = card_categories.category_id WHERE card_categories.card_id = :cardId")
    List<CategoryModel> getCategoriesByCardId(int cardId);

    @Query("SELECT * FROM keywords INNER JOIN card_keywords ON keywords.keywordid = card_keywords.keyword_id WHERE card_keywords.card_id = :cardId")
    List<KeywordModel> getKeywordsByCardId(int cardId);

    @Query("SELECT * FROM loyalties INNER JOIN card_loyalties ON loyalties.loyaltyid = card_loyalties.loyalty_id WHERE card_loyalties.card_id = :cardId")
    List<LoyaltyModel> getLoyaltiesByCardId(int cardId);

    @Query("SELECT * FROM cards INNER JOIN related_cards ON cards.cardid = related_cards.related_card_id WHERE related_cards.card_id = :cardId")
    List<CardModel> getRelatedCards(int cardId);

//    @Query("SELECT * FROM cards WHERE card_name LIKE '%'||:name||'%'")
//    LiveData<List<CardModel>> findCardsByName(String name);
//
//    @Query("SELECT * FROM cards WHERE faction_id IN (:factionIds)")
//    LiveData<List<CardModel>> findCardsByFilters(int[] factionIds);
}
