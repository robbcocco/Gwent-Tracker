package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.robbcocco.gwenttracker.database.entity.SetModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SetModel> sets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(SetModel... sets);

    @Query("SELECT setid FROM sets WHERE set_name LIKE :name LIMIT 1")
    int getIdByName(String name);
}
