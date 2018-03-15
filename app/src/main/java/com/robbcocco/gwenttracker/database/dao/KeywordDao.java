package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.KeywordModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface KeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<KeywordModel> keywords);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(KeywordModel... keywords);

    @Query("SELECT keywordid FROM keywords WHERE keyword_tag LIKE :tag LIMIT 1")
    int getIdByTag(String tag);
}
