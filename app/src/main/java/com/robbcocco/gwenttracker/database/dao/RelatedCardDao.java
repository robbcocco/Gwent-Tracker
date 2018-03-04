package com.robbcocco.gwenttracker.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.robbcocco.gwenttracker.database.entity.RelatedCardModel;

import java.util.List;

/**
 * Created by rober on 2/27/2018.
 */

@Dao
public interface RelatedCardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RelatedCardModel> relatedCards);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(RelatedCardModel... relatedCards);
}
