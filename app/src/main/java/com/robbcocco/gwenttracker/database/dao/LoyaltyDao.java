package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.LoyaltyModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface LoyaltyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LoyaltyModel> loyalties);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(LoyaltyModel... loyalties);

    @Query("SELECT loyaltyid FROM loyalties WHERE loyalty_name LIKE :name LIMIT 1")
    int getIdByName(String name);
}
