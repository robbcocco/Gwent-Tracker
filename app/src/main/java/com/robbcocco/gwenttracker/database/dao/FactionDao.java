package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.FactionModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface FactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<FactionModel> factions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(FactionModel... factions);

    @Query("SELECT factionid FROM factions WHERE faction_tag LIKE :tag LIMIT 1")
    int getIdByTag(String tag);

    @Query("SELECT * FROM factions")
    List<FactionModel> loadAllFactions();
}
