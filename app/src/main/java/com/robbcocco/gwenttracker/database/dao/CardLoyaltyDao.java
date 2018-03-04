package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.robbcocco.gwenttracker.database.entity.CardLoyaltyModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface CardLoyaltyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CardLoyaltyModel> cardLoyalties);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CardLoyaltyModel... cardLoyalties);
}
