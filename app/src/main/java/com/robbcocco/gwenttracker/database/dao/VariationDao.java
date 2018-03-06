package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.RarityModel;
import com.robbcocco.gwenttracker.database.entity.SetModel;
import com.robbcocco.gwenttracker.database.entity.VariationModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface VariationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VariationModel> cards);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(VariationModel... cards);

    @Query("SELECT * FROM variations")
    LiveData<List<VariationModel>> loadAllVariations();

    @Query("SELECT * FROM variations WHERE variationid = :id LIMIT 1")
    LiveData<VariationModel> findVariationById(int id);

    @Query("SELECT * FROM variations WHERE card_id = :cardId")
    LiveData<List<VariationModel>> findVariationsByCardId(int cardId);

    @Query("SELECT * FROM rarities WHERE rarityid = :rarityId LIMIT 1")
    LiveData<RarityModel> findRarityById(int rarityId);

    @Query("SELECT * FROM sets WHERE setid = :setId LIMIT 1")
    LiveData<SetModel> findSetById(int setId);
}
