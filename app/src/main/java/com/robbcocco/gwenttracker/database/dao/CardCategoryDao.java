package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.robbcocco.gwenttracker.database.entity.CardCategoryModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface CardCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CardCategoryModel> cardCategories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(CardCategoryModel... cardCategories);
}
