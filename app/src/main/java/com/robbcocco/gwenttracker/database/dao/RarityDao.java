package com.robbcocco.gwenttracker.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.RarityModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface RarityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RarityModel> rarities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RarityModel... rarities);

    @Query("SELECT rarityid FROM rarities WHERE rarity_name LIKE :name LIMIT 1")
    int getIdByName(String name);

    @Query("SELECT DISTINCT rarities.* FROM rarities INNER JOIN variations WHERE variations.rarity_id = rarities.rarityid")
    List<RarityModel> loadAllRarities();

    @Query("SELECT * FROM rarities WHERE rarityid = :rarityId LIMIT 1")
    LiveData<RarityModel> findRarityById(int rarityId);
}
